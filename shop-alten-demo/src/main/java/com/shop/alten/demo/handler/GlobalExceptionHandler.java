package com.shop.alten.demo.handler;

import com.shop.alten.demo.dto.RequestErrorDto;
import com.shop.alten.demo.exception.ResourceNotFoundException;
import com.shop.alten.demo.exception.UnauthorizedException;
import com.shop.alten.demo.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private String now() {
        return DateTimeFormatter.ISO_INSTANT
                .withZone(ZoneOffset.UTC)
                .format(Instant.now());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<RequestErrorDto> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new RequestErrorDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), now()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<RequestErrorDto> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new RequestErrorDto(ex.getMessage(), HttpStatus.NOT_FOUND.value(), now()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RequestErrorDto> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new RequestErrorDto("Invalid email or password", HttpStatus.UNAUTHORIZED.value(), now()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<RequestErrorDto> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new RequestErrorDto(ex.getMessage(), HttpStatus.UNAUTHORIZED.value(), now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RequestErrorDto> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new RequestErrorDto("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), now()));
    }
}
