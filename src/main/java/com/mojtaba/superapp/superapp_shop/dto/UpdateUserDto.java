package com.mojtaba.superapp.superapp_shop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateUserDto {
    @Email(message = "فرمت ایمیل نامعتبر است")
    private String email;

    @Pattern(regexp = "^\\+93[7][0-9]\\d{7}$", message = "فرمت شماره تلفن افغانی نامعتبر است")
    private String phone;
}