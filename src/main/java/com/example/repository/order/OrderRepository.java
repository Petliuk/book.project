package com.example.repository.order;

import com.example.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems "
            + "WHERE o.user.id = :userId AND o.isDeleted = false")
    Page<Order> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

}
