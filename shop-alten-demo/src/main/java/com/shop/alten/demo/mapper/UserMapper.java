package com.shop.alten.demo.mapper;

import com.shop.alten.demo.dto.UserDto;
import com.shop.alten.demo.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDTO(User user);

    User toEntity(UserDto dto);

}
