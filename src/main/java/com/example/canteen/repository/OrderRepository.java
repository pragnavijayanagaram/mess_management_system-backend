package com.example.canteen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.canteen.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClientId(Long clientId);
}