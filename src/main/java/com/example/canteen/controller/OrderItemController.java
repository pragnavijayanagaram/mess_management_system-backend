package com.example.canteen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.canteen.model.OrderItem;
import com.example.canteen.repository.OrderItemRepository;

@RestController
@RequestMapping("/order-items")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderItemController {

    @Autowired
    private OrderItemRepository orderItemRepository;

    // Save order item (used when placing order)
    @PostMapping
    public OrderItem saveItem(@RequestBody OrderItem item){

        System.out.println("Saving Item: " + item.getFoodItemId());

        return orderItemRepository.save(item);
    }

    // Get all order items
    @GetMapping
    public List<OrderItem> getAllItems(){
        return orderItemRepository.findAll();
    }

    // Get items by orderId (VERY IMPORTANT for Order History)
    @GetMapping("/order/{orderId}")
    public List<OrderItem> getItemsByOrder(@PathVariable Long orderId){
        return orderItemRepository.findByOrderId(orderId);
    }

}