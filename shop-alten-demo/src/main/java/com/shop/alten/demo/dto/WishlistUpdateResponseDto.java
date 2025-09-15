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
public class WishlistUpdateResponseDto {

    private WishlistDto wishlistDto;
    private List<WishlistItemErrorDto> errors;
}
