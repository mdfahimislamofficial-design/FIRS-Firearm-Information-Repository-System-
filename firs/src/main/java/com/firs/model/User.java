package com.firs.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String dob;
    private String nid;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private String role = "CUSTOMER";

    @Column(name = "badge_number")
    private String badgeNumber;

    private String department;

    @Column(name = "ffl_number")
    private String fflNumber;

    @Column(name = "business_name")
    private String businessName;

    private String status = "PENDING";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDob() {
        return dob;
    }

    public String getNid() {
        return nid;
    }

    public String getComment() {
        return comment;
    }

    public String getRole() {
        return role;
    }

    public String getBadgeNumber() {
        return badgeNumber;
    }

    public String getDepartment() {
        return department;
    }

    public String getFflNumber() {
        return fflNumber;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setBadgeNumber(String badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setFflNumber(String fflNumber) {
        this.fflNumber = fflNumber;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}