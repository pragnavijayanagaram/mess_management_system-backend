package com.example.canteen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.canteen.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByEmailAndPassword(String email, String password);

}