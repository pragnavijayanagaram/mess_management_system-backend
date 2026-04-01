package com.example.canteen.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.canteen.model.Client;
import com.example.canteen.repository.ClientRepository;
import com.example.canteen.utils.JwtUtil;

@RestController
@RequestMapping("/clients")
@CrossOrigin(origins = "http://localhost:3000")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    // Register new client
    @PostMapping("/register")
    public Client register(@RequestBody Client client) {
        return clientRepository.save(client);
    }

    // Login client
    @PostMapping("/login")
    public org.springframework.http.ResponseEntity<?> login(@RequestBody Client client){
        List<Client> users = clientRepository.findAll();
        for(Client c : users){
            if(c.getEmail().equals(client.getEmail()) &&
               c.getPassword().equals(client.getPassword())){
                
                String token = JwtUtil.generateToken(c.getEmail());
                java.util.Map<String, Object> response = new java.util.HashMap<>();
                response.put("token", token);
                response.put("clientId", c.getId());
                response.put("name", c.getName());
                response.put("role", c.getRole());
                return org.springframework.http.ResponseEntity.ok(response);
            }
        }  

        return org.springframework.http.ResponseEntity.status(401).body(java.util.Collections.singletonMap("error", "INVALID"));
    }

}