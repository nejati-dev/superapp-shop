package com.mojtaba.superapp.superapp_shop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateUserDto {
    @Email(message = "فرمت ایمیل نامعتبر است")
    @NotBlank(message = "ایمیل نمی‌تواند خالی باشد")
    private String email;

    @NotBlank(message = "شماره تلفن نمی‌تواند خالی باشد")
    @Pattern(regexp = "^\\+93[7][0-9]\\d{7}$", message = "فرمت شماره تلفن افغانی نامعتبر است")
    private String phone;

    @NotBlank(message = "رمز عبور نمی‌تواند خالی باشد")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "رمز عبور باید حداقل ۸ کاراکتر و شامل حرف و عدد باشد")
    private String password;
}