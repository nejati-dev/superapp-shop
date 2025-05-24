package com.mojtaba.superapp.superapp_shop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;
import java.time.Instant;

@Data
@Entity
@Table(name = "addresses", schema = "superapp")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String label;

    @Column(name = "full_address", columnDefinition = "TEXT")
    private String fullAddress;

    private String city;
    private String province;
    private String postalCode;

    @Column(columnDefinition = "GEOGRAPHY(Point,4326)")
    private Point location;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}