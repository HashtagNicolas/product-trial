package com.shop.alten.demo.service.impl;

import com.shop.alten.demo.dto.AccountDto;
import com.shop.alten.demo.entity.User;
import com.shop.alten.demo.mapper.AccountMapper;
import com.shop.alten.demo.repository.AccountRepository;
import com.shop.alten.demo.repository.UserRepository;
import com.shop.alten.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    private final AccountMapper accountMapper;

    @Override
    public AccountDto getAccountByEmailUser(String email) {
        User user = userRepository.findByEmail(email);
        return accountRepository.findByUser(user)
                .map(accountMapper::toDTO)
                .orElse(null);
    }
}
