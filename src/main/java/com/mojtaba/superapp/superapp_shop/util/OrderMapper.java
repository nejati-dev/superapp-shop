package com.mojtaba.superapp.superapp_shop.util;

import com.mojtaba.superapp.superapp_shop.dto.OrderDto;
import com.mojtaba.superapp.superapp_shop.dto.OrderItemDto;
import com.mojtaba.superapp.superapp_shop.dto.OrderDetailDto;
import com.mojtaba.superapp.superapp_shop.entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;
    private final OrderDetailMapper orderDetailMapper;

    public OrderMapper(OrderItemMapper orderItemMapper,
                       OrderDetailMapper orderDetailMapper) {
        this.orderItemMapper = orderItemMapper;
        this.orderDetailMapper = orderDetailMapper;
    }

    /**
     * تبدیل از Entity Order به OrderDto
     */
    public OrderDto toDto(Order order) {
        if (order == null) return null;

        OrderDto dto = new OrderDto();
        dto.setOrderId(order.getOrderId());
        dto.setUserId(order.getUser().getUserId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus().name());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        dto.setUpdatedById(order.getUpdatedBy() != null ? order.getUpdatedBy().getUserId() : null);

        // نگاشت لیست آیتم‌ها
        List<OrderItemDto> items = order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
        dto.setOrderItems(items);

        // نگاشت لیست جزئیات (در صورت وجود)
        if (order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()) {
            List<OrderDetailDto> details = order.getOrderDetails().stream()
                    .map(orderDetailMapper::toDto)
                    .collect(Collectors.toList());
            dto.setOrderDetails(details);
        }

        return dto;
    }
}
