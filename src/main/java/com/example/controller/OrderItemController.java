package com.example.controller;

import com.example.dto.order.OrderItemResponseDto;
import com.example.service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders/{orderId}/items")
@RequiredArgsConstructor
@Tag(name = "Order Item Controller", description = "Manage order items within an order")
public class OrderItemController {
    private final OrderItemService orderItemService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get all order items",
            description = "Retrieve all items for a specific order")
    public List<OrderItemResponseDto> getOrderItems(@PathVariable Long orderId) {
        return orderItemService.getOrderItems(orderId);
    }

    @GetMapping("/{itemId}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get specific order item",
            description = "Retrieve a specific item within an order")
    public OrderItemResponseDto getOrderItem(@PathVariable Long orderId,
                                             @PathVariable("itemId") Long itemId) {
        return orderItemService.getOrderItem(orderId, itemId);
    }
}
