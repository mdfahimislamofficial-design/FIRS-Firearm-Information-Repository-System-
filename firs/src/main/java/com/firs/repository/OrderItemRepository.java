package com.firs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.firs.model.OrderItem;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);
}