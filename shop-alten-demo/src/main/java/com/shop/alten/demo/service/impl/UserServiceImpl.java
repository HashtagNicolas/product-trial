package com.shop.alten.demo.service.impl;

import com.shop.alten.demo.dto.UserDto;
import com.shop.alten.demo.entity.Account;
import com.shop.alten.demo.entity.Cart;
import com.shop.alten.demo.entity.User;
import com.shop.alten.demo.entity.Wishlist;
import com.shop.alten.demo.mapper.UserMapper;
import com.shop.alten.demo.repository.UserRepository;
import com.shop.alten.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) throws UsernameNotFoundException {
        User user = userMapper.toEntity(userDto);
        user.setAccount(getInitAccount(user));
        return userMapper.toDTO(userRepository.save(user));
    }

    private static Account getInitAccount(User user) {
        Account account = new Account();
        account.setUser(user);
        Cart cart = new Cart();
        account.setCart(cart);
        Wishlist wishlist = new Wishlist();
        account.setWishlist(wishlist);
        return account;
    }

    @Override
    public UserDto findByEmail(String email) {
        return userMapper.toDTO(userRepository.findByEmail(email));
    }
}
