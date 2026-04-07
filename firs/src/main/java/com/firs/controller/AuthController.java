package com.firs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.firs.model.User;
import com.firs.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // REGISTER - INSERT INTO users
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        Map<String, Object> res = new HashMap<>();

        // Check if email already exists using SELECT query
        if (userRepository.existsByEmail(user.getEmail())) {
            res.put("status", "error");
            res.put("message", "Email already exists");
            return ResponseEntity.badRequest().body(res);
        }

        // Validate required fields
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            res.put("status", "error");
            res.put("message", "Name is required");
            return ResponseEntity.badRequest().body(res);
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            res.put("status", "error");
            res.put("message", "Email is required");
            return ResponseEntity.badRequest().body(res);
        }

        if (user.getPassword() == null || user.getPassword().length() < 6) {
            res.put("status", "error");
            res.put("message", "Password must be at least 6 characters");
            return ResponseEntity.badRequest().body(res);
        }

        // Set default values
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("CUSTOMER");
        }
        user.setRole(user.getRole().toUpperCase());
        user.setStatus("PENDING");

        // INSERT INTO users
        User savedUser = userRepository.save(user);

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", savedUser.getId());
        userData.put("name", savedUser.getName());
        userData.put("email", savedUser.getEmail());
        userData.put("role", savedUser.getRole());

        res.put("status", "success");
        res.put("message", "Registration successful");
        res.put("user", userData);

        return ResponseEntity.ok(res);
    }

    // LOGIN - SELECT from users
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        Map<String, Object> res = new HashMap<>();

        if (user.getEmail() == null || user.getPassword() == null) {
            res.put("status", "error");
            res.put("message", "Email and password required");
            return ResponseEntity.badRequest().body(res);
        }

        // SELECT * FROM users WHERE email = ? AND password = ?
        Optional<User> found = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());

        if (found.isPresent()) {
            User loggedUser = found.get();

            Map<String, Object> userData = new HashMap<>();
            userData.put("id", loggedUser.getId());
            userData.put("name", loggedUser.getName());
            userData.put("email", loggedUser.getEmail());
            userData.put("role", loggedUser.getRole());
            userData.put("status", loggedUser.getStatus());

            res.put("status", "success");
            res.put("message", "Login successful");
            res.put("user", userData);

            return ResponseEntity.ok(res);
        } else {
            res.put("status", "error");
            res.put("message", "Invalid email or password");
            return ResponseEntity.status(401).body(res);
        }
    }

    // GET USER BY ID - SELECT with WHERE clause
    @GetMapping("/user/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        Map<String, Object> res = new HashMap<>();
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.get().getId());
            userData.put("name", user.get().getName());
            userData.put("email", user.get().getEmail());
            userData.put("role", user.get().getRole());
            userData.put("status", user.get().getStatus());

            res.put("status", "success");
            res.put("user", userData);
            return ResponseEntity.ok(res);
        } else {
            res.put("status", "error");
            res.put("message", "User not found");
            return ResponseEntity.notFound().build();
        }
    }

    // UPDATE USER - UPDATE query
    @PutMapping("/user/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @RequestBody User userData) {
        Map<String, Object> res = new HashMap<>();

        Optional<User> existing = userRepository.findById(id);

        if (existing.isEmpty()) {
            res.put("status", "error");
            res.put("message", "User not found");
            return ResponseEntity.notFound().build();
        }

        User user = existing.get();

        // Update fields
        if (userData.getName() != null)
            user.setName(userData.getName());
        if (userData.getDob() != null)
            user.setDob(userData.getDob());
        if (userData.getNid() != null)
            user.setNid(userData.getNid());
        if (userData.getBadgeNumber() != null)
            user.setBadgeNumber(userData.getBadgeNumber());
        if (userData.getDepartment() != null)
            user.setDepartment(userData.getDepartment());
        if (userData.getFflNumber() != null)
            user.setFflNumber(userData.getFflNumber());
        if (userData.getBusinessName() != null)
            user.setBusinessName(userData.getBusinessName());
        if (userData.getStatus() != null)
            user.setStatus(userData.getStatus());

        // UPDATE users SET ... WHERE id = ?
        User updated = userRepository.save(user);

        res.put("status", "success");
        res.put("message", "User updated successfully");
        res.put("user", updated);

        return ResponseEntity.ok(res);
    }

    // GET ALL USERS - SELECT * FROM users (Admin only)
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status) {

        Map<String, Object> res = new HashMap<>();

        Iterable<User> users;

        if (role != null) {
            // SELECT * FROM users WHERE role = ?
            users = userRepository.findByRole(role.toUpperCase());
        } else if (status != null) {
            // SELECT * FROM users WHERE status = ?
            users = userRepository.findByStatus(status.toUpperCase());
        } else {
            // SELECT * FROM users
            users = userRepository.findAll();
        }

        res.put("status", "success");
        res.put("users", users);
        res.put("count", ((java.util.Collection<?>) users).size());

        return ResponseEntity.ok(res);
    }

    // DELETE USER - DELETE FROM users (Admin only)
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        Map<String, Object> res = new HashMap<>();

        if (!userRepository.existsById(id)) {
            res.put("status", "error");
            res.put("message", "User not found");
            return ResponseEntity.notFound().build();
        }

        // DELETE FROM users WHERE id = ?
        userRepository.deleteById(id);

        res.put("status", "success");
        res.put("message", "User deleted successfully");

        return ResponseEntity.ok(res);
    }
}