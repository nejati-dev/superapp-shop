package com.mojtaba.superapp.superapp_shop.repository;

import com.mojtaba.superapp.superapp_shop.entity.OrderStatusHistory;
import com.mojtaba.superapp.superapp_shop.entity.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {
    /**
     * ستون entity_id در دیتابیس، معادل فیلد entityId در جاواست.
     * entityType همیشه 'order' خواهد بود.
     */
    List<OrderStatusHistory> findByEntityTypeAndEntityId(EntityType entityType, Long entityId);
}
