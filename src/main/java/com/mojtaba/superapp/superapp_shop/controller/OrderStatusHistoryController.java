package com.mojtaba.superapp.superapp_shop.controller;

import com.mojtaba.superapp.superapp_shop.dto.OrderStatusHistoryDto;
import com.mojtaba.superapp.superapp_shop.entity.EntityType;
import com.mojtaba.superapp.superapp_shop.service.OrderStatusHistoryService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/status-history")
public class OrderStatusHistoryController {

    private final OrderStatusHistoryService historyService;

    public OrderStatusHistoryController(OrderStatusHistoryService historyService) {
        this.historyService = historyService;
    }

    /**
     * GET /api/status-history?entityType=order&entityId=123
     * دریافت تاریخچهٔ وضعیت برای یک سفارش
     */
    @GetMapping
    public ResponseEntity<List<OrderStatusHistoryDto>> getHistory(
            @RequestParam("entityType") @NotNull EntityType entityType,
            @RequestParam("entityId") @NotNull Long entityId) {

        List<OrderStatusHistoryDto> list = historyService.getStatusHistory(entityType, entityId);
        return ResponseEntity.ok(list);
    }
}
