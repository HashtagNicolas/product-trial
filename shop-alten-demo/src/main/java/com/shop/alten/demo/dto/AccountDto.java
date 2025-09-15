package com.shop.alten.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountDto {

    private Long id;

    private String email;

    private Long cartId;

    private Long wishlistId;
}
