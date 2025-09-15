package com.shop.alten.demo.mapper;

import com.shop.alten.demo.dto.WishlistItemDto;
import com.shop.alten.demo.entity.WishlistItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WishListItemMapper {

    @Mapping(target = "productId", source = "product.id")
    WishlistItemDto toDTO(WishlistItem wishlistItem);

    @Mapping(target = "product", ignore = true)
    WishlistItem toEntity(WishlistItemDto dto);
}
