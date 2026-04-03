package com.firs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.firs.model.User;
import com.firs.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository repo;

    // REGISTER
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody User user) {
        Map<String, String> res = new HashMap<>();

        if (repo.findByEmail(user.getEmail()) != null) {
            res.put("status", "error");
            res.put("message", "Email already exists");
            return res;
        }

        repo.save(user);
        res.put("status", "success");
        return res;
    }

    // LOGIN
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user) {
        Map<String, Object> res = new HashMap<>();

        User found = repo.findByEmailAndPassword(user.getEmail(), user.getPassword());

        if (found != null) {
            res.put("status", "success");
            res.put("user", found);
        } else {
            res.put("status", "error");
        }

        return res;
    }
}