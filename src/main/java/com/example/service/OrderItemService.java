package com.example.service;

import com.example.dto.order.OrderItemResponseDto;
import java.util.List;

public interface OrderItemService {
    List<OrderItemResponseDto> getOrderItems(Long orderId);

    OrderItemResponseDto getOrderItem(Long orderId, Long itemId);
}
