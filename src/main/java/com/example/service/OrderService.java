package com.example.service;

import com.example.dto.order.OrderItemResponseDto;
import com.example.dto.order.OrderRequestDto;
import com.example.dto.order.OrderResponseDto;
import com.example.dto.order.OrderStatusUpdateDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto placeOrder(Long userId, OrderRequestDto requestDto);

    List<OrderResponseDto> getOrderHistory(Long userId, Pageable pageable);

    OrderResponseDto updateOrderStatus(Long orderId, OrderStatusUpdateDto updateDto);

    List<OrderItemResponseDto> getOrderItems(Long orderId);

    OrderItemResponseDto getOrderItem(Long orderId, Long itemId);
}
