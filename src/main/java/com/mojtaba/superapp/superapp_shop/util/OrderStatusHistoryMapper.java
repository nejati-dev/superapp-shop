package com.mojtaba.superapp.superapp_shop.util;

import com.mojtaba.superapp.superapp_shop.dto.OrderStatusHistoryDto;
import com.mojtaba.superapp.superapp_shop.entity.OrderStatusHistory;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusHistoryMapper {

    /**
     * تبدیل از Entity OrderStatusHistory به OrderStatusHistoryDto
     */
    public OrderStatusHistoryDto toDto(OrderStatusHistory history) {
        if (history == null) return null;

        OrderStatusHistoryDto dto = new OrderStatusHistoryDto();
        dto.setHistoryId(history.getHistoryId());
        dto.setEntityType(history.getEntityType().name());
        dto.setEntityId(history.getEntityId());
        dto.setOldStatus(history.getOldStatus().name());
        dto.setNewStatus(history.getNewStatus().name());
        dto.setChangedById(history.getChangedBy().getUserId());
        dto.setChangedAt(history.getChangedAt());
        return dto;
    }
}
