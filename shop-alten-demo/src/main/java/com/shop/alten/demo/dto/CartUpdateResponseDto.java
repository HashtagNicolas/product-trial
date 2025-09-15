package com.shop.alten.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartUpdateResponseDto {
    private CartDto cartDto;
    private List<CartItemErrorDto> errors;
}