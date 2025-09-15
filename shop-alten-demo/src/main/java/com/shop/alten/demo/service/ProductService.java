package com.shop.alten.demo.service;

import com.shop.alten.demo.dto.ProductDto;

import java.util.List;

public interface ProductService {

    List<ProductDto> getAllProducts();

    ProductDto getProductById(Long id);

    ProductDto createProduct(ProductDto product);

    ProductDto updateProduct(Long id, ProductDto newProduct);

    void deleteProduct(Long id);

}
