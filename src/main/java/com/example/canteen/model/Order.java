package com.example.canteen.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;  

@Entity
@Table(name="orders")
public class Order {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(name="client_id")
private Long clientId;

@Column(name="pickup_time")
private String pickupTime;

private String status;

private double total;

// getters & setters

public Long getId(){ return id; }
public void setId(Long id){ this.id = id; }

public Long getClientId(){ return clientId; }
public void setClientId(Long clientId){ this.clientId = clientId; }

public String getPickupTime(){ return pickupTime; }
public void setPickupTime(String pickupTime){ this.pickupTime = pickupTime; }

public String getStatus(){ return status; }
public void setStatus(String status){ this.status = status; }

public double getTotal(){ return total; }
public void setTotal(double total){ this.total = total; }

}