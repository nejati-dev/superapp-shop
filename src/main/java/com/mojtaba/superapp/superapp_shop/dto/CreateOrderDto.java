package com.mojtaba.superapp.superapp_shop.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * DTO برای دریافت درخواست ایجاد سفارش
 * شامل شناسهٔ کاربر و لیست آیتم‌ها و (اختیاری) لیست جزئیات
 */
@Data
public class CreateOrderDto {

    @NotNull
    private Long userId;

    @NotEmpty
    @Valid
    private List<OrderItemDto> orderItems;

    @Valid
    private List<OrderDetailDto> orderDetails;
}
