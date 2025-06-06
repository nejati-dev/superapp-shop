package com.mojtaba.superapp.superapp_shop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_details", schema = "superapp")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long orderDetailId;

    /**
     * ستون order_id BIGINT NOT NULL REFERENCES superapp.orders(order_id) ON DELETE CASCADE
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_order_details_order"))
    private Order order;

    /**
     * ستون product_id INT NOT NULL REFERENCES superapp.products(product_id)
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_order_details_product"))
    private Product product;

    /**
     * ستون quantity INT NOT NULL CHECK(quantity>0)
     */
    @NotNull
    @Min(1)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * ستون unit_price NUMERIC(14,2) NOT NULL
     */
    @NotNull
    @DecimalMin("0.00")
    @Column(name = "unit_price", nullable = false, precision = 14, scale = 2)
    private BigDecimal unitPrice;
}

