package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.dto.CreateOrderDto;
import com.mojtaba.superapp.superapp_shop.dto.OrderDto;
import com.mojtaba.superapp.superapp_shop.dto.UpdateOrderDto;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(CreateOrderDto createOrderDto);
    OrderDto getOrderById(Long id);
    List<OrderDto> getAllOrders();
    List<OrderDto> getOrdersByUserId(Long userId);
    OrderDto updateOrderStatus(Long id, UpdateOrderDto updateOrderDto);
    void deleteOrder(Long id);
}
