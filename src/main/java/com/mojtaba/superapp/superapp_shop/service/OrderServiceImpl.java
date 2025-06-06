package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.dto.CreateOrderDto;
import com.mojtaba.superapp.superapp_shop.dto.OrderDto;
import com.mojtaba.superapp.superapp_shop.dto.UpdateOrderDto;
import com.mojtaba.superapp.superapp_shop.entity.*;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.repository.*;
import com.mojtaba.superapp.superapp_shop.util.OrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            OrderStatusHistoryRepository orderStatusHistoryRepository,
            OrderMapper orderMapper
    ) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderStatusHistoryRepository = orderStatusHistoryRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public OrderDto createOrder(CreateOrderDto createOrderDto) {
        // ۱. کاربر را پیدا کن
        User user = userRepository.findById(createOrderDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", createOrderDto.getUserId()));

        // ۲. تمام شناسه‌های محصول را استخراج کن.
        // نکته کلیدی: چون شناسه محصول در انتیتی Product از نوع Integer است،
        // شناسه‌های Long را از DTO گرفته و بلافاصله به Integer تبدیل می‌کنیم.
        List<Integer> productIds = Stream.concat(
                createOrderDto.getOrderItems().stream().map(dto -> dto.getProductId().intValue()),
                createOrderDto.getOrderDetails() != null ? createOrderDto.getOrderDetails().stream().map(dto -> dto.getProductId().intValue()) : Stream.empty()
        ).distinct().collect(Collectors.toList());

        // ۳. تمام محصولات را با یک کوئری بگیر. حالا productIds از نوع List<Integer> است و خطایی رخ نمی‌دهد.
        // فرض می‌کنیم کلید Map نیز Integer است.
        Map<Integer, Product> productMap = productRepository.findAllById(productIds)
                .stream()
                .collect(Collectors.toMap(Product::getProductId, product -> product));

        // ۴. موجودیت Order را بساز
        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.OrderStatus.pending);

        // ۵. لیست OrderItemها را بساز و به سفارش متصل کن
        List<OrderItem> items = createOrderDto.getOrderItems().stream()
                .map(itemDto -> {
                    // برای پیدا کردن در Map، شناسه Long را به Integer تبدیل می‌کنیم
                    Product product = productMap.get(itemDto.getProductId().intValue());
                    if (product == null) {
                        throw new ResourceNotFoundException("Product", "id", itemDto.getProductId());
                    }
                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setProduct(product);
                    item.setQuantity(itemDto.getQuantity());
                    item.setPrice(itemDto.getPrice());
                    return item;
                })
                .collect(Collectors.toList());
        order.setOrderItems(items);


// ۶. لیست OrderDetailها را (در صورت وجود) بساز و به سفارش متصل کن
        if (createOrderDto.getOrderDetails() != null && !createOrderDto.getOrderDetails().isEmpty()) {
            List<OrderDetail> details = createOrderDto.getOrderDetails().stream()
                    .map(detailDto -> {
                        // برای پیدا کردن در Map، شناسه Long را به Integer تبدیل می‌کنیم
                        Product product = productMap.get(detailDto.getProductId().intValue());
                        if (product == null) {
                            throw new ResourceNotFoundException("Product", "id", detailDto.getProductId());
                        }
                        OrderDetail detail = new OrderDetail();
                        detail.setOrder(order);
                        detail.setProduct(product);
                        detail.setQuantity(detailDto.getQuantity());
                        detail.setUnitPrice(detailDto.getUnitPrice());
                        return detail;
                    })
                    .collect(Collectors.toList());
            order.setOrderDetails(details);
        }

        // ۷. مبلغ کل سفارش را محاسبه کن
        BigDecimal totalAmount = calculateTotalAmount(order.getOrderItems());
        order.setTotalAmount(totalAmount);

        // ۸. سفارش را ذخیره کن
        Order savedOrder = orderRepository.save(order);

        // ۹. نتیجه را به DTO تبدیل و بازگردان
        return orderMapper.toDto(savedOrder);
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUser_UserId(userId);
        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDto updateOrderStatus(Long id, UpdateOrderDto updateOrderDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        Order.OrderStatus newStatus = Order.OrderStatus.valueOf(updateOrderDto.getStatus());

        User updatedBy = userRepository.findById(updateOrderDto.getUpdatedById())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User", "id", updateOrderDto.getUpdatedById()));

        if (!order.getStatus().equals(newStatus)) {
            OrderStatusHistory history = new OrderStatusHistory();
            history.setEntityType(EntityType.order);
            history.setEntityId(order.getOrderId());
            history.setOldStatus(order.getStatus());
            history.setNewStatus(newStatus);
            history.setChangedBy(updatedBy);
            orderStatusHistoryRepository.save(history);

            order.setStatus(newStatus);
        }

        order.setUpdatedBy(updatedBy);

        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toDto(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order", "id", id);
        }
        orderRepository.deleteById(id);
    }
}