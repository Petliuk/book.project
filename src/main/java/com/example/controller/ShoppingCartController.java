package com.example.controller;

import com.example.dto.cart.CartItemRequestDto;
import com.example.dto.cart.ShoppingCartDto;
import com.example.dto.cart.UpdateCartItemDto;
import com.example.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Shopping Cart Controller",
        description = "Manage shopping cart with CRUD operations and item management")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get shopping cart details",
            description = "Retrieve details of the current user's shopping cart")
    public ShoppingCartDto get(Authentication authentication) {
        Long userId = shoppingCartService.getUserId(authentication);
        return shoppingCartService.get(userId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Add an item to the shopping cart",
            description = "Add a new item to the user's shopping cart")
    public ShoppingCartDto saveItem(Authentication authentication,
                                @RequestBody @Valid CartItemRequestDto createDto) {
        return shoppingCartService.save(authentication, createDto);
    }

    @PutMapping("cart-items/{cartItemId}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Update the quantity of a cart item",
            description = "Modify the quantity of an existing item in the shopping cart")
    public ShoppingCartDto updateItemQuantity(Authentication authentication,
                                          @PathVariable Long cartItemId,
                                          @RequestBody @Valid UpdateCartItemDto updateDto) {
        return shoppingCartService.update(authentication, cartItemId, updateDto);
    }

    @DeleteMapping("cart-items/{cartItemId}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Remove an item from the shopping cart",
            description = "Delete an item from the shopping cart by its ID")
    public void deleteItemById(Authentication authentication, @PathVariable Long cartItemId) {
        Long userId = shoppingCartService.getUserId(authentication);
        shoppingCartService.deleteById(userId, cartItemId);
    }
}
