package com.shop.alten.demo.service;

import com.shop.alten.demo.dto.AccountDto;

public interface AccountService {
    AccountDto getAccountByEmailUser(String email);
}
