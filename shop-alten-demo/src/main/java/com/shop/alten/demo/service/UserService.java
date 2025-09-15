package com.shop.alten.demo.service;

import com.shop.alten.demo.dto.UserDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

    UserDto createUser(UserDto userDto) throws UsernameNotFoundException;

    UserDto findByEmail(String username);
}
