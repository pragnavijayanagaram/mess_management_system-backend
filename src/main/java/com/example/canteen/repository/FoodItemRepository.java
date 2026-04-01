package com.example.canteen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.canteen.model.FoodItem;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

}