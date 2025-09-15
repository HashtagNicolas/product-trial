package com.shop.alten.demo.controller;

import com.shop.alten.demo.dto.WishlistDto;
import com.shop.alten.demo.dto.WishlistUpdateResponseDto;
import com.shop.alten.demo.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishListController {

    private final WishListService wishListService;

    @GetMapping("/{id}")
    public ResponseEntity<WishlistDto> getCardById(@PathVariable Long id) {
        WishlistDto wishlistDto = wishListService.getWishListById(id);
        return wishlistDto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(wishlistDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WishlistUpdateResponseDto> updateCard(@PathVariable Long id, @RequestBody WishlistDto wishlistDto) {
        return ResponseEntity.ok(wishListService.updateWishList(id, wishlistDto));
    }
}
