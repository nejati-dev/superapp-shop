package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.dto.OrderStatusHistoryDto;
import com.mojtaba.superapp.superapp_shop.entity.EntityType;
import com.mojtaba.superapp.superapp_shop.entity.OrderStatusHistory;
import com.mojtaba.superapp.superapp_shop.repository.OrderStatusHistoryRepository;
import com.mojtaba.superapp.superapp_shop.service.OrderStatusHistoryService;
import com.mojtaba.superapp.superapp_shop.util.OrderStatusHistoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * پیاده‌سازی OrderStatusHistoryService
 */
@Service
public class OrderStatusHistoryServiceImpl implements OrderStatusHistoryService {

    private final OrderStatusHistoryRepository historyRepository;
    private final OrderStatusHistoryMapper historyMapper;

    public OrderStatusHistoryServiceImpl(
            OrderStatusHistoryRepository historyRepository,
            OrderStatusHistoryMapper historyMapper
    ) {
        this.historyRepository = historyRepository;
        this.historyMapper = historyMapper;
    }

    /**
     * دریافت لیست تاریخچهٔ وضعیت برای یک نهاد مشخص
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderStatusHistoryDto> getStatusHistory(EntityType entityType, Long entityId) {
        List<OrderStatusHistory> histories = historyRepository.findByEntityTypeAndEntityId(entityType, entityId);
        return histories.stream()
                .map(historyMapper::toDto)
                .collect(Collectors.toList());
    }
}
