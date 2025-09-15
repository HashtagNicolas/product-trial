package com.shop.alten.demo.mapper;

import com.shop.alten.demo.dto.ProductDto;
import com.shop.alten.demo.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDTO(Product product);

    Product toEntity(ProductDto dto);
}
