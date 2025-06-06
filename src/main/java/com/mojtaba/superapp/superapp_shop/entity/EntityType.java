package com.mojtaba.superapp.superapp_shop.entity;

/**
 * نگاشت به نوع ENUM دیتابیس superapp.entity_type_enum
 * حداقل شامل 'order' است. در آینده برای سایر جدول‌ها (ride, delivery, …) هم اضافه کنید.
 */
public enum EntityType {
    order,
    ride,
    delivery,
    driver,
    payment,
    user,
    product
}

