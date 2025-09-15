package com.shop.alten.demo.controller;

import com.shop.alten.demo.configuration.JwtUtils;
import com.shop.alten.demo.dto.AuthResponseDto;
import com.shop.alten.demo.dto.UserDto;
import com.shop.alten.demo.exception.UserAlreadyExistsException;
import com.shop.alten.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody @Valid UserDto userDto) {
        if (userService.findByEmail(userDto.getEmail()) != null) {
            throw new UserAlreadyExistsException("User already exists - Email : " + userDto.getEmail());
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        UserDto createdUser = userService.createUser(userDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid UserDto userDto) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));

            if (authentication.isAuthenticated()) {
                String token = jwtUtils.generateToken(userDto.getEmail());
                return ResponseEntity.ok(new AuthResponseDto("Bearer",token));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } catch (AuthenticationException e) {
            log.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
