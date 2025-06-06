package com.mojtaba.superapp.superapp_shop.controller;

import com.mojtaba.superapp.superapp_shop.dto.CreateOrderDto;
import com.mojtaba.superapp.superapp_shop.dto.OrderDto;
import com.mojtaba.superapp.superapp_shop.dto.UpdateOrderDto;
import com.mojtaba.superapp.superapp_shop.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * POST /api/orders
     * ایجاد سفارش جدید
     */
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(
            @Valid @RequestBody CreateOrderDto createOrderDto) {

        OrderDto created = orderService.createOrder(createOrderDto);
        return ResponseEntity.status(201).body(created);
    }

    /**
     * GET /api/orders/{id}
     * دریافت جزئیات یک سفارش
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        OrderDto dto = orderService.getOrderById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * GET /api/orders
     * دریافت همهٔ سفارش‌ها
     */
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> list = orderService.getAllOrders();
        return ResponseEntity.ok(list);
    }

    /**
     * GET /api/orders/user/{userId}
     * دریافت سفارش‌های یک کاربر
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderDto> list = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(list);
    }

    /**
     * PUT /api/orders/{id}
     * به‌روزرسانی وضعیت سفارش
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderDto updateOrderDto) {

        OrderDto updated = orderService.updateOrderStatus(id, updateOrderDto);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/orders/{id}
     * حذف یک سفارش
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
