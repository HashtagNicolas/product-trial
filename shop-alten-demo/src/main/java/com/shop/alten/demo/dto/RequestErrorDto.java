package com.shop.alten.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestErrorDto {

    private String message;

    private int status;

    private String timestamp;
}
