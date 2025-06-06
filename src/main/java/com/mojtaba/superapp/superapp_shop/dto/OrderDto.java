package com.mojtaba.superapp.superapp_shop.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * DTO برای نمایش اطلاعات کامل یک سفارش
 * شامل اطلاعات سفارش، لیست آیتم‌ها و لیست جزئیات اختیاری
 */
@Data
public class OrderDto {
    private Long orderId;
    private Long userId;
    private BigDecimal totalAmount;
    private String status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Long updatedById;
    private List<OrderItemDto> orderItems;
    private List<OrderDetailDto> orderDetails; // اگر از جزئیات استفاده می‌شود
}
