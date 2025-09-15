package com.shop.alten.demo.mapper;

import com.shop.alten.demo.dto.CartItemDto;
import com.shop.alten.demo.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "productId", source = "product.id")
    CartItemDto toDTO(CartItem cartItem);

    @Mapping(target = "product.id", source = "productId")
    CartItem toEntity(CartItemDto dto);

}
