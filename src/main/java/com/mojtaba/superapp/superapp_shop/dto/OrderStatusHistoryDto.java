package com.mojtaba.superapp.superapp_shop.dto;

import lombok.Data;

import java.time.OffsetDateTime;

/**
 * DTO برای نمایش رکوردهای تاریخچهٔ وضعیت سفارش
 */
@Data
public class OrderStatusHistoryDto {
    private Long historyId;
    private String entityType;
    private Long entityId;
    private String oldStatus;
    private String newStatus;
    private Long changedById;
    private OffsetDateTime changedAt;
}
