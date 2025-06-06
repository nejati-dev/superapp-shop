package com.mojtaba.superapp.superapp_shop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orders", schema = "superapp")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    /**
     * جدول superapp.orders ستون user_id دارد که به superapp.users(user_id) FK است
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * total_amount NUMERIC(14,2) NOT NULL CHECK(total_amount > 0)
     */
    @NotNull
    @DecimalMin("0.01")
    @Column(name = "total_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalAmount;

    /**
     * status superapp.status_order_enum NOT NULL
     * در جاوا از Enum با نگاشت STRING استفاده می‌کنیم.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatus status;

    /**
     * created_at TIMESTAMPTZ NOT NULL DEFAULT now()
     * نگاشت به OffsetDateTime تا منطقهٔ زمانی حفظ شود.
     * @CreationTimestamp باعث می‌شود در اولین ذخیرهٔ Entity، مقدار now() روی این فیلد گذاشته شود.
     */
    @NotNull
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    /**
     * updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
     * @UpdateTimestamp باعث می‌شود در هر به‌روزرسانی، مجدداً now() ثبت شود.
     */
    @NotNull
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    /**
     * updated_by BIGINT REFERENCES superapp.users(user_id)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", foreignKey = @ForeignKey(name = "fk_orders_updated_by"))
    private User updatedBy;

    /**
     * رابطهٔ یک‌به‌چند با order_items
     * در جدول order_items ستون order_id ON DELETE CASCADE دارد.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * رابطهٔ یک‌به‌چند با order_details
     * جدول order_details ستون order_id ON DELETE CASCADE دارد.
     * این بخش اختیاری است؛ در صورت استفاده باید Entity زیر را تعریف کنید.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public enum OrderStatus {
        pending,
        processing,
        shipped,
        delivered,
        cancelled
    }
}

