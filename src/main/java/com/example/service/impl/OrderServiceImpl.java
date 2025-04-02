package com.example.service.impl;

import com.example.dto.order.OrderItemResponseDto;
import com.example.dto.order.OrderRequestDto;
import com.example.dto.order.OrderResponseDto;
import com.example.dto.order.OrderStatusUpdateDto;
import com.example.mapper.OrderItemMapper;
import com.example.mapper.OrderMapper;
import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.model.OrderStatus;
import com.example.model.ShoppingCart;
import com.example.repository.cart.ShoppingCartRepository;
import com.example.repository.order.OrderItemRepository;
import com.example.repository.order.OrderRepository;
import com.example.service.OrderService;
import com.example.service.ShoppingCartService;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartService shoppingCartService;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderItemResponseDto> getOrderItems(Long orderId) {
        return orderItemRepository.findAllByOrderId(orderId)
                .stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemResponseDto getOrderItem(Long orderId, Long itemId) {
        return orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .map(orderItemMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Order item not found with ID: "
                        + itemId + " in order: " + orderId));
    }

    @Override
    @Transactional
    public OrderResponseDto placeOrder(Long userId, OrderRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUserId(userId);
        if (shoppingCart.getCartItems() == null || shoppingCart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cannot place an order with an empty shopping cart.");
        }
        Order order = createOrder(shoppingCart, requestDto);
        addOrderItemsFromCart(shoppingCart, order);
        Order savedOrder = orderRepository.save(order);
        shoppingCartService.clearCart(userId);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderResponseDto> getOrderHistory(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable)
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatusUpdateDto updateDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()
                        -> new EntityNotFoundException("Order not found with ID: " + orderId));
        order.setStatus(OrderStatus.valueOf(updateDto.getStatus().toUpperCase()));
        return orderMapper.toDto(orderRepository.save(order));
    }

    private BigDecimal calculateTotal(ShoppingCart shoppingCart) {
        return shoppingCart.getCartItems().stream()
                .map(cartItem -> cartItem.getBook()
                        .getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Order createOrder(ShoppingCart shoppingCart, OrderRequestDto requestDto) {
        return Order.builder()
                .user(shoppingCart.getUser())
                .status(OrderStatus.PENDING)
                .total(calculateTotal(shoppingCart))
                .orderDate(LocalDateTime.now())
                .shippingAddress(requestDto.getShippingAddress())
                .build();
    }

    private void addOrderItemsFromCart(ShoppingCart shoppingCart, Order order) {
        shoppingCart.getCartItems().forEach(cartItem -> {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .book(cartItem.getBook())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getBook().getPrice())
                    .build();
            order.getOrderItems().add(orderItem);
        });
    }
}
