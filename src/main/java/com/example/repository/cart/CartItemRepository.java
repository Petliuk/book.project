package com.example.repository.cart;

import com.example.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndShoppingCartId(Long itemId, Long cartId);

    void deleteAllByShoppingCartId(Long shoppingCartId);
}
