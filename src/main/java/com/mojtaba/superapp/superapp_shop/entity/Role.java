package com.mojtaba.superapp.superapp_shop.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles", schema = "superapp")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @Column(nullable = false, unique = true, length = 50)
    private String roleName;
}
