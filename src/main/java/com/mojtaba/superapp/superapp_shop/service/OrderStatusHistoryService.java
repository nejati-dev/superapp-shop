package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.dto.OrderStatusHistoryDto;
import com.mojtaba.superapp.superapp_shop.entity.EntityType;

import java.util.List;

/**
 * سرویس برای کار با تاریخچهٔ وضعیت
 */
public interface OrderStatusHistoryService {
    /**
     * دریافت لیست تاریخچهٔ وضعیت برای یک نهاد مشخص
     *
     * @param entityType نوع نهاد (مثلاً EntityType.order)
     * @param entityId   شناسهٔ آن نهاد
     * @return لیستی از OrderStatusHistoryDto
     */
    List<OrderStatusHistoryDto> getStatusHistory(EntityType entityType, Long entityId);
}
