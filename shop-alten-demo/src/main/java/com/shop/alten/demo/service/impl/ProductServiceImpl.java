package com.shop.alten.demo.service.impl;

import com.shop.alten.demo.dto.ProductDto;
import com.shop.alten.demo.entity.Product;
import com.shop.alten.demo.enums.InventoryStatus;
import com.shop.alten.demo.mapper.ProductMapper;
import com.shop.alten.demo.repository.ProductRepository;
import com.shop.alten.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    @Override
    public ProductDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDTO)
                .orElse(null);
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        Product saved = productRepository.save(product);
        return productMapper.toDTO(saved);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto newProductDto) {
        return productRepository.findById(id).map(existing -> {
            existing.setName(newProductDto.getName());
            existing.setDescription(newProductDto.getDescription());
            existing.setPrice(newProductDto.getPrice());
            existing.setQuantity(newProductDto.getQuantity());
            existing.setCategory(newProductDto.getCategory());
            existing.setInventoryStatus(InventoryStatus.valueOf(newProductDto.getInventoryStatus()));
            existing.setUpdatedAt(java.time.LocalDateTime.now());
            return productMapper.toDTO(productRepository.save(existing));
        }).orElse(null);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
