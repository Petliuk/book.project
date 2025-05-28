package com.example.controller;

import com.example.dto.order.OrderItemResponseDto;
import com.example.dto.order.OrderRequestDto;
import com.example.dto.order.OrderResponseDto;
import com.example.dto.order.OrderStatusUpdateDto;
import com.example.model.User;
import com.example.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order Controller", description = "Manage orders with CRUD operations")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Place an order",
            description = "Create a new order from the user's shopping cart")
    public OrderResponseDto placeOrder(Authentication authentication,
                                       @RequestBody @Valid OrderRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.placeOrder(user.getId(), requestDto);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get order history",
            description = "Retrieve the order history for the authenticated user")
    public List<OrderResponseDto> getOrderHistory(Authentication authentication,
                                                  Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderHistory(user.getId(), pageable);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update order status",
            description = "Update the status of an existing order (Admin only)")
    public OrderResponseDto updateOrderStatus(@PathVariable Long id,
                                              @RequestBody @Valid OrderStatusUpdateDto updateDto) {
        return orderService.updateOrderStatus(id, updateDto);
    }

    @GetMapping("/orders/{orderId}/items")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get all order items",
            description = "Retrieve all items for a specific order")
    public List<OrderItemResponseDto> getOrderItems(@PathVariable Long orderId) {
        return orderService.getOrderItems(orderId);
    }

    @GetMapping("/{itemId}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get specific order item",
            description = "Retrieve a specific item within an order")
    public OrderItemResponseDto getOrderItem(@PathVariable Long orderId,
                                             @PathVariable("itemId") Long itemId) {
        return orderService.getOrderItem(orderId, itemId);
    }
}
