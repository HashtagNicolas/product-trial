package com.shop.alten.demo.controller;

import com.shop.alten.demo.dto.CartDto;
import com.shop.alten.demo.dto.CartUpdateResponseDto;
import com.shop.alten.demo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCardById(@PathVariable Long id) {
        CartDto cartDto = cartService.getCardById(id);
        return cartDto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(cartDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartUpdateResponseDto> updateCard(@PathVariable Long id, @RequestBody CartDto cartDto) {
        return ResponseEntity.ok(cartService.updateCart(id, cartDto));
    }

}
