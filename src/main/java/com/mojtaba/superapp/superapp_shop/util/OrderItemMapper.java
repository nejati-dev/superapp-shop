package com.mojtaba.superapp.superapp_shop.util;

import com.mojtaba.superapp.superapp_shop.dto.OrderItemDto;
import com.mojtaba.superapp.superapp_shop.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    /**
     * تبدیل از Entity OrderItem به OrderItemDto
     */
    public OrderItemDto toDto(OrderItem item) {
        if (item == null) return null;

        OrderItemDto dto = new OrderItemDto();
        dto.setOrderItemId(item.getOrderItemId());
        dto.setProductId(item.getProduct().getProductId().longValue());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        return dto;
    }
}
