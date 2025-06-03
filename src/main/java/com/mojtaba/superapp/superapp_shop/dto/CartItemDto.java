package com.mojtaba.superapp.superapp_shop.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CartItemDto {
    private Long cartItemId;
    private Integer productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;
    private Instant addedAt;
}

