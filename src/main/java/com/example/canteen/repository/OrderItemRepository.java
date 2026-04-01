package com.example.canteen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.example.canteen.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // ✅ Get all items for a specific order
    List<OrderItem> findByOrderId(Long orderId);

}