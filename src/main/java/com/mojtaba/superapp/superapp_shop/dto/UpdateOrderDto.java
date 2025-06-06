package com.mojtaba.superapp.superapp_shop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * DTO برای دریافت درخواست به‌روزرسانی وضعیت سفارش
 */
@Data
public class UpdateOrderDto {

    @NotNull
    @Pattern(regexp = "pending|processing|shipped|delivered|cancelled",
            message = "مقادیر مجاز: pending, processing, shipped, delivered, cancelled")
    private String status;

    @NotNull
    private Long updatedById;
}
