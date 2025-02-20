package com.example.service;

import com.example.dto.cart.CartItemDto;
import com.example.dto.cart.CartItemRequestDto;
import com.example.dto.cart.ShoppingCartDto;
import com.example.dto.cart.UpdateCartItemDto;
import com.example.model.ShoppingCart;
import com.example.model.User;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {

    ShoppingCartDto get(Authentication authentication);

    CartItemDto save(Authentication authentication, CartItemRequestDto requestDto);

    CartItemDto update(Authentication authentication, Long cartItemId, UpdateCartItemDto quantity);

    void deleteById(Authentication authentication, Long cartItemId);

    ShoppingCart create(User user);
}
