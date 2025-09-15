package com.shop.alten.demo.service;

import com.shop.alten.demo.dto.CartDto;
import com.shop.alten.demo.dto.CartUpdateResponseDto;

public interface CartService {

    CartUpdateResponseDto updateCart(Long id, CartDto dto);

    CartDto getCardById(Long id);
}
