package com.firs.controller;

import com.firs.model.User;
import com.firs.repository.UserRepository;
import com.firs.util.PasswordUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
@RequestMapping("/api/auth")
public class AuthController {

    private static final Set<String> SELF_REGISTRATION_ROLES = Set.of("CUSTOMER", "DEALER", "GOVERNMENT");

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user, HttpSession session) {
        Map<String, Object> validation = validateRegistration(user);
        if (validation != null) {
            return ResponseEntity.badRequest().body(validation);
        }

        String normalizedEmail = user.getEmail().trim().toLowerCase(Locale.ROOT);
        if (userRepository.existsByEmail(normalizedEmail)) {
            return badRequest("Email already exists");
        }

        String normalizedRole = normalizeRole(user.getRole());
        if (!SELF_REGISTRATION_ROLES.contains(normalizedRole)) {
            return badRequest("Selected role is not allowed for self registration");
        }

        user.setEmail(normalizedEmail);
        user.setName(user.getName().trim());
        user.setRole(normalizedRole);
        user.setPassword(PasswordUtil.hash(user.getPassword().trim()));
        user.setStatus("CUSTOMER".equals(normalizedRole) ? "APPROVED" : "PENDING");

        User savedUser = userRepository.save(user);
        setSession(session, savedUser);

        Map<String, Object> res = ok("Registration successful");
        res.put("user", publicUser(savedUser));
        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user, HttpSession session) {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return badRequest("Email is required");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return badRequest("Password is required");
        }

        Optional<User> userOpt = userRepository.findByEmail(user.getEmail().trim().toLowerCase(Locale.ROOT));
        if (userOpt.isEmpty()) {
            return unauthorized("Email not found");
        }

        User loggedUser = userOpt.get();
        if (!PasswordUtil.matches(user.getPassword(), loggedUser.getPassword())) {
            return unauthorized("Invalid email or password");
        }

        if (PasswordUtil.needsHashing(loggedUser.getPassword())) {
            loggedUser.setPassword(PasswordUtil.hash(user.getPassword().trim()));
            loggedUser = userRepository.save(loggedUser);
        }

        setSession(session, loggedUser);

        Map<String, Object> res = ok("Login successful");
        res.put("user", publicUser(loggedUser));
        return ResponseEntity.ok(res);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(ok("Logged out successfully"));
    }

    @GetMapping("/session")
    public ResponseEntity<Map<String, Object>> getSession(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            Map<String, Object> res = new HashMap<>();
            res.put("loggedIn", false);
            return ResponseEntity.ok(res);
        }

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            session.invalidate();
            Map<String, Object> res = new HashMap<>();
            res.put("loggedIn", false);
            return ResponseEntity.ok(res);
        }

        Map<String, Object> res = ok("Session active");
        res.put("loggedIn", true);
        res.put("user", publicUser(user.get()));
        return ResponseEntity.ok(res);
    }

    private Map<String, Object> validateRegistration(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            return error("Name is required");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return error("Email is required");
        }
        if (!user.getEmail().contains("@")) {
            return error("Enter a valid email address");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return error("Password is required");
        }
        if (user.getPassword().trim().length() < 6) {
            return error("Password must be at least 6 characters");
        }
        return null;
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return "CUSTOMER";
        }
        return role.trim().toUpperCase(Locale.ROOT);
    }

    private void setSession(HttpSession session, User user) {
        session.setAttribute("userId", user.getId());
        session.setAttribute("userRole", user.getRole());
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("userName", user.getName());
    }

    private Map<String, Object> publicUser(User user) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("name", user.getName());
        userData.put("email", user.getEmail());
        userData.put("role", user.getRole());
        userData.put("status", user.getStatus());
        userData.put("phoneNumber", user.getPhoneNumber());
        return userData;
    }

    private Map<String, Object> ok(String message) {
        Map<String, Object> res = new HashMap<>();
        res.put("status", "success");
        res.put("message", message);
        return res;
    }

    private Map<String, Object> error(String message) {
        Map<String, Object> res = new HashMap<>();
        res.put("status", "error");
        res.put("message", message);
        return res;
    }

    private ResponseEntity<Map<String, Object>> badRequest(String message) {
        return ResponseEntity.badRequest().body(error(message));
    }

    private ResponseEntity<Map<String, Object>> unauthorized(String message) {
        return ResponseEntity.status(401).body(error(message));
    }
}
