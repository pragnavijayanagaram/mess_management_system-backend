package com.example.canteen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.canteen.model.FoodItem;
import com.example.canteen.repository.FoodItemRepository;

@RestController
@RequestMapping("/food-items")
@CrossOrigin(origins = "http://localhost:3000")
public class FoodItemController {

    @Autowired
    private FoodItemRepository foodItemRepository;

    // Get all food items
    @GetMapping
    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();
    }

    // Add food item
    @PostMapping
    public FoodItem addFoodItem(@RequestBody FoodItem foodItem) {
        return foodItemRepository.save(foodItem);
    }

    @GetMapping("/{id}")
    public FoodItem getFoodById(@PathVariable Long id){
        return foodItemRepository.findById(id).orElse(null);
    }
}