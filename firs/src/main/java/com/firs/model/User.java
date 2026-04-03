package com.firs.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String dob;
    private String nid;
    private String comment;

    // ✅ GETTERS

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

    // ✅ SETTERS

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
}