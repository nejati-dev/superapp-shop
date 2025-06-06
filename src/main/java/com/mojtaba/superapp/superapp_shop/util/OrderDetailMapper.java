package com.mojtaba.superapp.superapp_shop.util;

import com.mojtaba.superapp.superapp_shop.dto.OrderDetailDto;
import com.mojtaba.superapp.superapp_shop.entity.OrderDetail;
import org.springframework.stereotype.Component;

@Component
public class OrderDetailMapper {

    /**
     * تبدیل از Entity OrderDetail به OrderDetailDto
     */
    public OrderDetailDto toDto(OrderDetail detail) {
        if (detail == null) return null;

        OrderDetailDto dto = new OrderDetailDto();
        dto.setOrderDetailId(detail.getOrderDetailId());
        dto.setProductId(detail.getProduct().getProductId().longValue());
        dto.setQuantity(detail.getQuantity());
        dto.setUnitPrice(detail.getUnitPrice());
        return dto;
    }
}
