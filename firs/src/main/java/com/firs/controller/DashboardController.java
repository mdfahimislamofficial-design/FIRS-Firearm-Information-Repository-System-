package com.firs.controller;

import com.firs.model.Order;
import com.firs.model.Product;
import com.firs.model.User;
import com.firs.repository.OrderRepository;
import com.firs.repository.ProductRepository;
import com.firs.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/admin")
    public ResponseEntity<Map<String, Object>> adminDashboard(HttpSession session) {
        if (!hasRole(session, "ADMIN")) {
            return unauthorized("Admin access required");
        }

        List<User> users = userRepository.findAll();
        List<Product> products = productRepository.findAll();
        List<Order> orders = orderRepository.findAll();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalUsers", users.size());
        summary.put("customers", countUsersByRole(users, "CUSTOMER"));
        summary.put("dealers", countUsersByRole(users, "DEALER"));
        summary.put("government", countUsersByRole(users, "GOVERNMENT"));
        summary.put("pendingUsers", countUsersByStatus(users, "PENDING"));
        summary.put("approvedUsers", countUsersByStatus(users, "APPROVED"));
        summary.put("products", products.size());
        summary.put("lowStockProducts", products.stream().filter(p -> safeInt(p.getStock()) <= 10).count());
        summary.put("orders", orders.size());
        summary.put("pendingOrders", orders.stream().filter(o -> "PENDING".equalsIgnoreCase(o.getStatus())).count());
        summary.put("revenue", orders.stream().mapToDouble(o -> o.getTotalAmount() == null ? 0 : o.getTotalAmount()).sum());

        Map<String, Object> res = ok();
        res.put("summary", summary);
        res.put("recentUsers", users.stream()
                .sorted(Comparator.comparing(User::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(6)
                .collect(Collectors.toList()));
        res.put("recentOrders", orders.stream()
                .sorted(Comparator.comparing(Order::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(6)
                .collect(Collectors.toList()));
        res.put("lowStockProducts", products.stream()
                .filter(p -> safeInt(p.getStock()) <= 10)
                .sorted(Comparator.comparing(Product::getStock, Comparator.nullsLast(Comparator.naturalOrder())))
                .limit(6)
                .collect(Collectors.toList()));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/dealer")
    public ResponseEntity<Map<String, Object>> dealerDashboard(HttpSession session) {
        if (!hasRole(session, "DEALER")) {
            return unauthorized("Dealer access required");
        }

        List<Product> products = productRepository.findAll();
        List<Order> orders = orderRepository.findAll();
        List<Product> listedProducts = products.stream().limit(8).collect(Collectors.toList());

        Map<String, Object> summary = new HashMap<>();
        summary.put("activeListings", products.size());
        summary.put("lowStock", products.stream().filter(p -> safeInt(p.getStock()) <= 10).count());
        summary.put("approvedListings", products.stream().filter(p -> safeInt(p.getStock()) > 0).count());
        summary.put("pendingReview", 0);
        summary.put("salesCount", orders.size());
        summary.put("salesValue", orders.stream().mapToDouble(o -> o.getTotalAmount() == null ? 0 : o.getTotalAmount()).sum());

        Map<String, Object> res = ok();
        res.put("summary", summary);
        res.put("inventory", listedProducts);
        res.put("recentOrders", orders.stream()
                .sorted(Comparator.comparing(Order::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(6)
                .collect(Collectors.toList()));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/government")
    public ResponseEntity<Map<String, Object>> governmentDashboard(HttpSession session) {
        if (!hasRole(session, "GOVERNMENT")) {
            return unauthorized("Government access required");
        }

        List<User> users = userRepository.findAll();
        List<Order> orders = orderRepository.findAll();
        List<Product> products = productRepository.findAll();

        Map<String, Object> summary = new HashMap<>();
        summary.put("pendingLicenses", countUsersByStatus(users, "PENDING"));
        summary.put("verifiedDealers", countUsersByRoleAndStatus(users, "DEALER", "APPROVED"));
        summary.put("flaggedOrders", orders.stream().filter(o -> "PENDING".equalsIgnoreCase(o.getStatus())).count());
        summary.put("restrictedInventory", products.stream().filter(p -> isRestrictedType(p.getType())).count());

        Map<String, Object> res = ok();
        res.put("summary", summary);
        res.put("verificationQueue", users.stream()
                .filter(u -> !"APPROVED".equalsIgnoreCase(u.getStatus()))
                .limit(8)
                .collect(Collectors.toList()));
        res.put("restrictedProducts", products.stream()
                .filter(p -> isRestrictedType(p.getType()))
                .limit(8)
                .collect(Collectors.toList()));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/customer")
    public ResponseEntity<Map<String, Object>> customerDashboard(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return unauthorized("Login required");
        }

        List<Order> orders = orderRepository.findUserOrders(userId);
        Optional<User> userOpt = userRepository.findById(userId);
        Map<String, Object> summary = new HashMap<>();
        summary.put("orders", orders.size());
        summary.put("pendingOrders", orders.stream().filter(o -> "PENDING".equalsIgnoreCase(o.getStatus())).count());
        summary.put("completedOrders", orders.stream().filter(o -> "DELIVERED".equalsIgnoreCase(o.getStatus()) || "COMPLETED".equalsIgnoreCase(o.getStatus())).count());
        summary.put("spent", orders.stream().mapToDouble(o -> o.getTotalAmount() == null ? 0 : o.getTotalAmount()).sum());
        summary.put("accountStatus", userOpt.map(User::getStatus).orElse("UNKNOWN"));

        Map<String, Object> res = ok();
        res.put("summary", summary);
        res.put("recentOrders", orders.stream().limit(5).collect(Collectors.toList()));
        return ResponseEntity.ok(res);
    }

    private boolean hasRole(HttpSession session, String requiredRole) {
        Object role = session.getAttribute("userRole");
        return role != null && requiredRole.equalsIgnoreCase(String.valueOf(role));
    }

    private ResponseEntity<Map<String, Object>> unauthorized(String message) {
        Map<String, Object> res = new HashMap<>();
        res.put("status", "error");
        res.put("message", message);
        return ResponseEntity.status(401).body(res);
    }

    private Map<String, Object> ok() {
        Map<String, Object> res = new HashMap<>();
        res.put("status", "success");
        return res;
    }

    private long countUsersByRole(List<User> users, String role) {
        return users.stream().filter(u -> role.equalsIgnoreCase(u.getRole())).count();
    }

    private long countUsersByStatus(List<User> users, String status) {
        return users.stream().filter(u -> status.equalsIgnoreCase(u.getStatus())).count();
    }

    private long countUsersByRoleAndStatus(List<User> users, String role, String status) {
        return users.stream().filter(u -> role.equalsIgnoreCase(u.getRole()) && status.equalsIgnoreCase(u.getStatus())).count();
    }

    private boolean isRestrictedType(String type) {
        return Objects.equals(type, "RIFLE") || Objects.equals(type, "SNIPER") || Objects.equals(type, "SHOTGUN");
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value;
    }
}
