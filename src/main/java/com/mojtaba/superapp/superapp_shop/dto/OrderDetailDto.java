package com.mojtaba.superapp.superapp_shop.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO برای نمایش یا دریافت جزئیات سفارش (اختیاری)
 */
@Data
public class OrderDetailDto {
    private Long orderDetailId; // فقط برای پاسخ‌های GET خالی است

    @NotNull
    private Long productId;     // شناسهٔ محصول

    @NotNull
    @Min(1)
    private Integer quantity;   // تعداد در جزئیات

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal unitPrice; // قیمت واحد در جزئیات
}
