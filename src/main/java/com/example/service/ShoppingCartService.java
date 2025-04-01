package com.example.service;

import com.example.dto.cart.CartItemRequestDto;
import com.example.dto.cart.ShoppingCartDto;
import com.example.dto.cart.UpdateCartItemDto;
import com.example.model.User;

public interface ShoppingCartService {

    ShoppingCartDto get(Long userId);

    ShoppingCartDto save(Long userId, CartItemRequestDto requestDto);

    ShoppingCartDto update(Long userId,
                           Long cartItemId,
                           UpdateCartItemDto updateDto);

    void deleteById(Long userId, Long cartItemId);

    void create(User user);

    void clearCart(Long userId);
}
