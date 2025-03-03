package com.example.service;

import com.example.dto.cart.CartItemRequestDto;
import com.example.dto.cart.ShoppingCartDto;
import com.example.dto.cart.UpdateCartItemDto;
import com.example.model.User;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {

    ShoppingCartDto get(Long userId);

    ShoppingCartDto save(Authentication authentication, CartItemRequestDto requestDto);

    ShoppingCartDto update(Authentication authentication,
                           Long cartItemId,
                           UpdateCartItemDto updateDto);

    void deleteById(Long userId, Long cartItemId);

    void create(User user);

    Long getUserId(Authentication authentication);
}
