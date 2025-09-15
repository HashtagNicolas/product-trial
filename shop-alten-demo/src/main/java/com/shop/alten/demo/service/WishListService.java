package com.shop.alten.demo.service;

import com.shop.alten.demo.dto.WishlistDto;
import com.shop.alten.demo.dto.WishlistUpdateResponseDto;

public interface WishListService {
    WishlistDto getWishListById(Long id);

    WishlistUpdateResponseDto updateWishList(Long id, WishlistDto wishlistDto);
}
