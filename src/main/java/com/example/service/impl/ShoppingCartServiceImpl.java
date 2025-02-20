package com.example.service.impl;

import com.example.dto.cart.CartItemDto;
import com.example.dto.cart.CartItemRequestDto;
import com.example.dto.cart.ShoppingCartDto;
import com.example.dto.cart.UpdateCartItemDto;
import com.example.exception.IllegalUserAccessException;
import com.example.mapper.CartItemMapper;
import com.example.mapper.ShoppingCartMapper;
import com.example.model.Book;
import com.example.model.CartItem;
import com.example.model.ShoppingCart;
import com.example.model.User;
import com.example.repository.book.BookRepository;
import com.example.repository.cart.CartItemRepository;
import com.example.repository.cart.ShoppingCartRepository;
import com.example.service.ShoppingCartService;
import com.example.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    private final UserService userService;

    @Override
    public ShoppingCartDto get(Authentication authentication) {
        ShoppingCart shoppingCart = getShoppingCartForUser(authentication);
        shoppingCart.setCartItems(Set.copyOf(cartItemRepository
                .findAllByShoppingCartId(shoppingCart.getId())));
        return cartMapper.toDto(shoppingCart);
    }

    @Override
    public CartItemDto save(Authentication authentication, CartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = getShoppingCartForUser(authentication);
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find a book by id "
                        + requestDto.getBookId()));
        CartItem cartItem = findOrCreateCartItem(shoppingCart, book, requestDto.getQuantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemDto update(Authentication authentication,
                              Long cartItemId, UpdateCartItemDto updateDto) {
        ShoppingCart shoppingCart = getShoppingCartForUser(authentication);
        CartItem cartItem = getCartItemIfExists(shoppingCart, cartItemId);
        cartItem.setQuantity(updateDto.getQuantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteById(Authentication authentication, Long cartItemId) {
        ShoppingCart shoppingCart = getShoppingCartForUser(authentication);
        getCartItemIfExists(shoppingCart, cartItemId);
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public ShoppingCart create(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }

    private ShoppingCart getShoppingCartForUser(Authentication authentication) {
        Long userId = userService.getAuthenticatedUser(authentication).getId();
        return shoppingCartRepository.getShoppingCartByUserId(userId);
    }

    private CartItem findOrCreateCartItem(ShoppingCart shoppingCart, Book book, int quantity) {
        return shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(book.getId()))
                .findAny()
                .map(existingItem -> {
                    existingItem.setQuantity(quantity);
                    return existingItem;
                })
                .orElseGet(() -> new CartItem(book, quantity, shoppingCart));
    }

    private CartItem getCartItemIfExists(ShoppingCart shoppingCart, Long cartItemId) {
        return shoppingCart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findAny()
                .orElseThrow(()
                        -> new IllegalUserAccessException("The user can't modify this cart item!"));
    }
}
