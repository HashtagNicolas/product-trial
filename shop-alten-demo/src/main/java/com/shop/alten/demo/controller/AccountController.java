package com.shop.alten.demo.controller;

import com.shop.alten.demo.dto.AccountDto;
import com.shop.alten.demo.service.AccountService;
import io.jsonwebtoken.Jwt;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping()
    public ResponseEntity<AccountDto> getAccountByEmail(@RequestParam @Valid String email) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String tokenEmail;

        if (principal instanceof UserDetails) {
            tokenEmail = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            tokenEmail = (String) principal;
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!tokenEmail.equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        AccountDto accountDto = accountService.getAccountByEmailUser(email);
        return accountDto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(accountDto);
    }
}
