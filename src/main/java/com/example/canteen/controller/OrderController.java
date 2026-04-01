package com.example.canteen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.canteen.model.Order;
import com.example.canteen.repository.OrderRepository;
import com.example.canteen.utils.JwtUtil;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;
    @PostMapping
    public Order placeOrder(@RequestBody Order order){
        Order savedOrder = orderRepository.save(order);
        System.out.println("Order saved with ID: " + savedOrder.getId());
        return savedOrder;
    }

    @PostMapping("/full")
    public org.springframework.http.ResponseEntity<?> placeFullOrder(@RequestBody java.util.Map<String, Object> payload) {
        try {
            Long clientId = Long.valueOf(payload.get("clientId").toString());
            String pickupTime = payload.get("pickupTime").toString();
            double total = Double.parseDouble(payload.get("total").toString());
            
            Order order = new Order();
            order.setClientId(clientId);
            order.setPickupTime(pickupTime);
            order.setStatus("PLACED");
            order.setTotal(total);
            
            Order savedOrder = orderRepository.save(order);
            
            List<java.util.Map<String, Object>> items = (List<java.util.Map<String, Object>>) payload.get("items");
            for(java.util.Map<String, Object> item : items) {
                com.example.canteen.model.OrderItem oi = new com.example.canteen.model.OrderItem();
                oi.setOrderId(savedOrder.getId());
                oi.setFoodItemId(Long.valueOf(item.get("id").toString()));
                
                // optional quantity, default 1
                Object qtyObj = item.get("quantity");
                oi.setQuantity(qtyObj != null ? Integer.parseInt(qtyObj.toString()) : 1);
                
                oi.setPrice(Double.parseDouble(item.get("price").toString()));
                orderItemRepository.save(oi);
            }
            return org.springframework.http.ResponseEntity.ok(savedOrder);
        } catch(Exception e) {
            e.printStackTrace();
            return org.springframework.http.ResponseEntity.status(500).body("Error processing order: " + e.getMessage());
        }
    }

    @Autowired
    private com.example.canteen.repository.ClientRepository clientRepository;

    @GetMapping
    public org.springframework.http.ResponseEntity<List<java.util.Map<String, Object>>> getAllOrders(@RequestHeader(value = "Authorization", required = false) String token) {
        List<Order> orders = orderRepository.findAll();
        // Sort descending by ID (newest first)
        orders.sort((a,b)->b.getId().compareTo(a.getId()));
        return org.springframework.http.ResponseEntity.ok(buildFullOrdersList(orders));
    }

    @GetMapping("/today")
    public org.springframework.http.ResponseEntity<List<java.util.Map<String, Object>>> getTodayOrders(@RequestHeader(value = "Authorization", required = false) String token) {
        // Since we don't have a date column yet, we'll return all for now or filter by some logic
        // As a simple hack without schema migration, we just return all PLACED orders as 'today'
        // or we simply return all orders since the assignment doesn't enforce a date field.
        List<Order> orders = orderRepository.findAll();
        orders.sort((a,b)->b.getId().compareTo(a.getId()));
        return org.springframework.http.ResponseEntity.ok(buildFullOrdersList(orders));
    }

    @PutMapping("/{id}/status")
    public org.springframework.http.ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody java.util.Map<String, String> body) {
        Order order = orderRepository.findById(id).orElse(null);
        if(order == null) return org.springframework.http.ResponseEntity.notFound().build();
        if(body.containsKey("status")) {
            order.setStatus(body.get("status"));
            orderRepository.save(order);
        }
        return org.springframework.http.ResponseEntity.ok(order);
    }

    private List<java.util.Map<String, Object>> buildFullOrdersList(List<Order> orders) {
        List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();
        for (Order o : orders) {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", o.getId());
            map.put("pickupTime", o.getPickupTime());
            map.put("status", o.getStatus());
            map.put("total", o.getTotal());
            
            // Get Client info
            com.example.canteen.model.Client client = clientRepository.findById(o.getClientId()).orElse(null);
            if(client != null) {
                map.put("clientName", client.getName());
                map.put("clientEmail", client.getEmail());
            } else {
                map.put("clientName", "Unknown");
                map.put("clientEmail", "Unknown");
            }
            
            List<com.example.canteen.model.OrderItem> items = orderItemRepository.findByOrderId(o.getId());
            List<java.util.Map<String, Object>> itemDetails = new java.util.ArrayList<>();
            
            for(com.example.canteen.model.OrderItem item : items) {
                 java.util.Map<String, Object> iMap = new java.util.HashMap<>();
                 iMap.put("quantity", item.getQuantity());
                 iMap.put("price", item.getPrice());
                 com.example.canteen.model.FoodItem food = foodItemRepository.findById(item.getFoodItemId()).orElse(null);
                 iMap.put("name", food != null ? food.getName() : "Unknown Item");
                 itemDetails.add(iMap);
            }
            map.put("items", itemDetails);
            result.add(map);
        }
        return result;
    }

    @Autowired
    private com.example.canteen.repository.OrderItemRepository orderItemRepository;
    
    @Autowired
    private com.example.canteen.repository.FoodItemRepository foodItemRepository;

    @GetMapping("/client/{clientId}/full")
    public org.springframework.http.ResponseEntity<List<java.util.Map<String, Object>>> getFullOrdersByClient(@PathVariable Long clientId, @RequestHeader(value = "Authorization", required = false) String token) {
        List<Order> orders = orderRepository.findByClientId(clientId);
        List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();

        for (Order o : orders) {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", o.getId());
            map.put("pickupTime", o.getPickupTime());
            map.put("status", o.getStatus());
            map.put("total", o.getTotal());
            
            List<com.example.canteen.model.OrderItem> items = orderItemRepository.findByOrderId(o.getId());
            List<java.util.Map<String, Object>> itemDetails = new java.util.ArrayList<>();
            
            for(com.example.canteen.model.OrderItem item : items) {
                 java.util.Map<String, Object> iMap = new java.util.HashMap<>();
                 iMap.put("quantity", item.getQuantity());
                 iMap.put("price", item.getPrice());
                 com.example.canteen.model.FoodItem food = foodItemRepository.findById(item.getFoodItemId()).orElse(null);
                 iMap.put("name", food != null ? food.getName() : "Unknown Item");
                 itemDetails.add(iMap);
            }
            map.put("items", itemDetails);
            result.add(map);
        }
        return org.springframework.http.ResponseEntity.ok(result);
    }

}