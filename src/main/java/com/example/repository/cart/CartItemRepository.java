package com.example.repository.cart;

import com.example.model.CartItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("FROM CartItem ci "
            + "LEFT JOIN FETCH ci.book "
            + "LEFT JOIN FETCH ci.shoppingCart "
            + "WHERE ci.id = :cartItemId "
            + "AND ci.isDeleted = false")
    Optional<CartItem> findById(Long id);

    @Query("FROM CartItem ci "
            + "LEFT JOIN FETCH ci.book "
            + "LEFT JOIN FETCH ci.shoppingCart "
            + "WHERE ci.shoppingCart.id = :id "
            + "AND ci.isDeleted = false")
    List<CartItem> findAllByShoppingCartId(Long id);

    Optional<CartItem> findByIdAndShoppingCartId(Long id, Long shoppingCartId);
}
