package com.shop.alten.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemErrorDto {
    private Long productId;
    private String codeErreur;
    private String message;
}

