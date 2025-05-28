package com.mojtaba.superapp.superapp_shop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "products", schema = "superapp")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @NotNull @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @NotNull @Column(unique = true, length = 100)
    private String sku;

    @NotNull @Positive
    private BigDecimal price;

    @NotNull
    private Instant createdAt;
    @NotNull
    private Instant updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductTranslation> translations;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductImage> images;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
