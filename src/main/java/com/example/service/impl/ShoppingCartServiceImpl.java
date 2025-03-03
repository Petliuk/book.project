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
import com.example.repository.user.UserRepository;
import com.example.service.ShoppingCartService;
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
    private final UserRepository userRepository;

    @Override
    public ShoppingCartDto get(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartByUserId(userId);
        if (shoppingCart == null) {
            throw new EntityNotFoundException("Shopping cart for user with ID "
                    + userId + " not found.");
        }
        shoppingCart.setCartItems(Set.copyOf(cartItemRepository
                .findAllByShoppingCartId(shoppingCart.getId())));
        return cartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto save(Authentication authentication, CartItemRequestDto requestDto) {
        Long userId = getUserId(authentication);
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartByUserId(userId);
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find a book by id "
                        + requestDto.getBookId()));
        CartItem cartItem = findOrCreateCartItem(shoppingCart, book, requestDto.getQuantity());
        cartItemRepository.save(cartItem);
        shoppingCart.setCartItems(Set.copyOf(cartItemRepository
                .findAllByShoppingCartId(shoppingCart.getId())));
        return cartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto update(Authentication authentication,
                                  Long cartItemId,
                                  UpdateCartItemDto updateDto) {
        Long userId = getUserId(authentication);
        ShoppingCart shoppingCart = shoppingCartRepository
                .getShoppingCartByUserId(userId);
        CartItem cartItem = cartItemRepository
                .findByIdAndShoppingCartId(cartItemId, shoppingCart.getId())
                .orElseThrow(()
                        -> new EntityNotFoundException("The user can't modify this cart item!"));
        cartItem.setQuantity(updateDto.getQuantity());
        cartItemRepository.save(cartItem);
        shoppingCart.setCartItems(Set.copyOf(cartItemRepository
                .findAllByShoppingCartId(shoppingCart.getId())));
        return cartMapper.toDto(shoppingCart);
    }

    @Override
    public void deleteById(Long userId, Long cartItemId) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .getShoppingCartByUserId(userId);
        CartItem cartItem = cartItemRepository
                .findByIdAndShoppingCartId(cartItemId, shoppingCart.getId())
                .orElseThrow(()
                        -> new EntityNotFoundException("The user can't modify this cart item!"));
        cartItemRepository.delete(cartItem);
    }

    public void create(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    private CartItem findOrCreateCartItem(ShoppingCart shoppingCart,
                                          Book book,
                                          int quantity) {
        return shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(book.getId()))
                .findAny()
                .map(existingItem -> {
                    existingItem.setQuantity(quantity);
                    return existingItem;
                })
                .orElseGet(() -> new CartItem(book, quantity, shoppingCart));
    }

    @Override
    public Long getUserId(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByEmail(username)
                .orElseThrow(()
                        -> new EntityNotFoundException("User not found")).getId();
    }
}
