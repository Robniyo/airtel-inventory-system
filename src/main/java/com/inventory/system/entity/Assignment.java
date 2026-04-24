package com.inventory.system.entity;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Airtel Inventory Management System
 * Entity representing the assignment of assets to employees.
 */
@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate issueDate;

    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ISSUED;

    // Establishing the link to the Asset entity
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    // Establishing the link to the User (Employee) entity
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Enum for tracking the lifecycle of an assignment.
     */
    public enum Status {
        ISSUED,
        RETURNED
    }

    // --- Constructors ---

    public Assignment() {}

    public Assignment(Asset asset, User user, LocalDate issueDate, Status status) {
        this.asset = asset;
        this.user = user;
        this.issueDate = issueDate;
        this.status = status;
    }

    // --- Getters and Setters ---

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public LocalDate getIssueDate() { 
        return issueDate; 
    }

    public void setIssueDate(LocalDate issueDate) { 
        this.issueDate = issueDate; 
    }

    public LocalDate getReturnDate() { 
        return returnDate; 
    }

    public void setReturnDate(LocalDate returnDate) { 
        this.returnDate = returnDate; 
    }

    public Status getStatus() { 
        return status; 
    }

    public void setStatus(Status status) { 
        this.status = status; 
    }

    public Asset getAsset() { 
        return asset; 
    }

    public void setAsset(Asset asset) { 
        this.asset = asset; 
    }

    public User getUser() { 
        return user; 
    }

    public void setUser(User user) { 
        this.user = user; 
    }
}