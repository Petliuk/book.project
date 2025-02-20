package com.example.repository.cart;

import com.example.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("FROM ShoppingCart sc"
            + " LEFT JOIN FETCH sc.user"
            + " LEFT JOIN FETCH sc.cartItems"
            + " WHERE sc.user.id = :id")
    ShoppingCart getShoppingCartByUserId(Long id);
}
