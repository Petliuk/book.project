package com.example.service.impl;

import com.example.dto.cart.CartItemRequestDto;
import com.example.dto.cart.ShoppingCartDto;
import com.example.dto.cart.UpdateCartItemDto;
import com.example.mapper.ShoppingCartMapper;
import com.example.model.Book;
import com.example.model.CartItem;
import com.example.model.ShoppingCart;
import com.example.model.User;
import com.example.repository.book.BookRepository;
import com.example.repository.cart.CartItemRepository;
import com.example.repository.cart.ShoppingCartRepository;
import com.example.service.ShoppingCartService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper cartMapper;

    @Override
    public ShoppingCartDto get(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .getShoppingCartByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart for user with ID " + userId + " not found."
                ));
        return cartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto save(Long userId, CartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .getShoppingCartByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart for user with ID " + userId + " not found."
                ));
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find a book by id "
                        + requestDto.getBookId()));
        CartItem newCartItem = CartItem.builder()
                .book(book)
                .quantity(requestDto.getQuantity())
                .shoppingCart(shoppingCart)
                .build();
        shoppingCart.getCartItems().add(newCartItem);
        shoppingCartRepository.save(shoppingCart);
        return cartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto update(Long userId,
                                  Long cartItemId,
                                  UpdateCartItemDto updateDto) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .getShoppingCartByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart for user with ID " + userId + " not found."
                ));
        CartItem cartItem = cartItemRepository
                .findByIdAndShoppingCartId(cartItemId, userId)
                .orElseThrow(()
                        -> new EntityNotFoundException("The user can't modify this cart item!"));
        cartItem.setQuantity(cartItem.getQuantity() + updateDto.getQuantity());
        shoppingCart.getCartItems().add(cartItem);
        shoppingCartRepository.save(shoppingCart);
        return cartMapper.toDto(shoppingCart);
    }

    @Override
    public void deleteById(Long userId, Long cartItemId) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .getShoppingCartByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart for user with ID " + userId + " not found."
                ));
        CartItem cartItem = cartItemRepository
                .findByIdAndShoppingCartId(cartItemId, shoppingCart.getId())
                .orElseThrow(()
                        -> new EntityNotFoundException("The user can't modify this cart item!"));
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void create(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void clearCart(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .getShoppingCartByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart for user with ID " + userId + " not found."
                ));
        cartItemRepository.deleteAllByShoppingCartId(shoppingCart.getId());
    }

    public ShoppingCart getShoppingCartByUserId(Long userId) {
        return shoppingCartRepository.getShoppingCartByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart for user with ID " + userId + " not found."
                ));
    }
}
