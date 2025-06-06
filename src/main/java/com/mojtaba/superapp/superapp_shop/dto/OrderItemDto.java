package com.mojtaba.superapp.superapp_shop.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO برای نمایش یا دریافت آیتم‌های سفارش
 */
@Data
public class OrderItemDto {
    private Long orderItemId; // فقط برای پاسخ‌های GET خالی است

    @NotNull
    private Long productId;   // شناسهٔ محصول

    @NotNull
    @Min(1)
    private Integer quantity; // تعداد

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal price; // قیمت واحد
}
