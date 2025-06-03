package com.mojtaba.superapp.superapp_shop.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class WishlistDto {
    private Long wishlistId;
    private Integer productId;
    private String productName;
    private BigDecimal productPrice;
    private Instant addedAt;
}

