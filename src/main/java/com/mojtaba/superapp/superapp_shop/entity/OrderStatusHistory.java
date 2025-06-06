package com.mojtaba.superapp.superapp_shop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "status_history", schema = "superapp")
public class OrderStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    /**
     * ستون entity_type superapp.entity_type_enum NOT NULL
     * در جاوا از Enum برای مطابقت کامل نوع استفاده می‌کنیم.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false, length = 20)
    private EntityType entityType = EntityType.order;

    /**
     * ستون entity_id BIGINT NOT NULL
     * در جاوای قبلی فیلد به نام orderId بود؛ حتماً باید نام ستون را با @Column اصلاح کنیم.
     */
    @NotNull
    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    /**
     * ستون old_status VARCHAR(30) NOT NULL
     * در دیتابیس این ستون صرفاً رشته است؛ اما ما از Enum جاوا نگاشت‌شده با STRING استفاده می‌کنیم.
     * باید نام ثابت‌های Enum با مقادیر پایگاه داده (حروف کوچک) یکسان باشد.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "old_status", nullable = false, length = 30)
    private Order.OrderStatus oldStatus;

    /**
     * ستون new_status VARCHAR(30) NOT NULL
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 30)
    private Order.OrderStatus newStatus;

    /**
     * ستون changed_by BIGINT NOT NULL REFERENCES superapp.users(user_id)
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by", nullable = false,
            foreignKey = @ForeignKey(name = "fk_status_history_changed_by"))
    private User changedBy;

    /**
     * ستون changed_at TIMESTAMPTZ NOT NULL DEFAULT now()
     * با @CreationTimestamp هنگام درج مقدار now() تنظیم می‌شود.
     */
    @NotNull
    @CreationTimestamp
    @Column(name = "changed_at", nullable = false, updatable = false)
    private OffsetDateTime changedAt;
}

