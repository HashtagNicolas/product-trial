package com.shop.alten.demo.mapper;

import com.shop.alten.demo.dto.CartDto;
import com.shop.alten.demo.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CartItemMapper.class)
public interface CartMapper {

    @Mapping(target = "cartItemDtoList", source = "items")
    CartDto toDTO(Cart cart);

    @Mapping(target = "items", source = "cartItemDtoList")
    Cart toEntity(CartDto dto);
}
