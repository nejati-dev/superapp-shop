package com.mojtaba.superapp.superapp_shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoleDto {
    @NotBlank(message = "Role name cannot be blank")
    @Size(max = 50, message = "Role name must be at most 50 characters")
    private String roleName;
}

