package com.inventory.system.entity;

import javax.persistence.*;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "assets")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String serialNumber;

    private String brand;
    private String category;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User assignedUser;

    @Enumerated(EnumType.STRING)
    private Status status = Status.AVAILABLE;

    public enum Status {
        AVAILABLE, ASSIGNED, UNDER_REPAIR
    }

    public Asset() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public User getAssignedUser() { return assignedUser; }
    public void setAssignedUser(User assignedUser) { this.assignedUser = assignedUser; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
