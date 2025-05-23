package com.mojtaba.superapp.superapp_shop.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "roles", schema = "superapp")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @Column(nullable = false, unique = true, length = 50)
    @EqualsAndHashCode.Include
    private String roleName;


    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}
