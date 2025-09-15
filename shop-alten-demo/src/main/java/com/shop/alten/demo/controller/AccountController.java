package com.shop.alten.demo.controller;

import com.shop.alten.demo.dto.AccountDto;
import com.shop.alten.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping()
    public ResponseEntity<AccountDto> getAccountByEmail(@RequestParam String email) {
        AccountDto accountDto = accountService.getAccountByEmailUser(email);
        return accountDto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(accountDto);
    }
}
