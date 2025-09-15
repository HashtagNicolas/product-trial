package com.shop.alten.demo.mapper;

import com.shop.alten.demo.dto.WishlistDto;
import com.shop.alten.demo.entity.Wishlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = WishListItemMapper.class)
public interface WishListMapper {

    @Mapping(target = "wishlistItemDtoList", source = "items")
    WishlistDto toDTO(Wishlist wishlist);

    @Mapping(target = "items", source = "wishlistItemDtoList")
    Wishlist toEntity(WishlistDto dto);
}
