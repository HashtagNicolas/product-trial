package com.shop.alten.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private Long id;

    private String code;

    private String name;

    private String description;

    private String image;

    private String category;

    private double price;

    private int quantity;

    private String internalReference;

    private Long shellId;

    private String inventoryStatus;

    private int rating;
}
