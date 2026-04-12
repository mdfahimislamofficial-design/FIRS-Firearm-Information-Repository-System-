package com.firs.controller;

import com.firs.model.Order;
import com.firs.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(error("Not logged in"));
        }

        String shippingAddress = request.get("shippingAddress") != null ? String.valueOf(request.get("shippingAddress")).trim() : null;
        Long fflDealerId = request.get("fflDealerId") instanceof Number number ? number.longValue() : null;

        if (shippingAddress == null || shippingAddress.isBlank()) {
            return ResponseEntity.badRequest().body(error("Shipping address is required"));
        }

        try {
            Order order = orderService.createOrder(userId, shippingAddress, fflDealerId);
            Map<String, Object> res = ok("Order created successfully");
            res.put("order", order);
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(error(ex.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getUserOrders(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(error("Not logged in"));
        }

        List<Order> orders = orderService.getUserOrders(userId);
        Map<String, Object> res = ok("Orders fetched successfully");
        res.put("orders", orders);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<Map<String, Object>> getOrder(@PathVariable String orderNumber, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(error("Not logged in"));
        }

        Optional<Order> order = orderService.getOrderByNumber(orderNumber);
        if (order.isEmpty()) {
            return ResponseEntity.status(404).body(error("Order not found"));
        }
        if (order.get().getUser() != null && !userId.equals(order.get().getUser().getId()) && !"ADMIN".equals(session.getAttribute("userRole"))) {
            return ResponseEntity.status(403).body(error("You do not have access to this order"));
        }

        Map<String, Object> res = ok("Order fetched successfully");
        res.put("order", order.get());
        return ResponseEntity.ok(res);
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
}
