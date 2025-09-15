package com.shop.alten.demo.mapper;

import com.shop.alten.demo.dto.AccountDto;
import com.shop.alten.demo.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "cartId", source = "cart.id")
    @Mapping(target = "wishlistId", source = "wishlist.id")
    AccountDto toDTO(Account account);

    @Mapping(target = "user.email", source = "email")
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "wishlist", ignore = true)
    Account toEntity(AccountDto dto);
}
