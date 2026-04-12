package com.firs.controller;

import com.firs.model.CartItem;
import com.firs.model.Product;
import com.firs.model.User;
import com.firs.repository.CartItemRepository;
import com.firs.repository.ProductRepository;
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
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCart(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return unauthorized();
        }

        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        double total = cartItems.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
        int itemCount = cartItems.stream().mapToInt(item -> item.getQuantity() == null ? 0 : item.getQuantity()).sum();

        Map<String, Object> res = new HashMap<>();
        res.put("status", "success");
        res.put("items", cartItems);
        res.put("total", total);
        res.put("count", itemCount);
        res.put("lineItems", cartItems.size());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToCart(@RequestBody Map<String, Object> request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return unauthorized();
        }

        if (request.get("productId") == null) {
            return badRequest("Product is required");
        }

        Long productId = ((Number) request.get("productId")).longValue();
        int quantity = request.get("quantity") instanceof Number number ? number.intValue() : 1;
        if (quantity <= 0) {
            return badRequest("Quantity must be at least 1");
        }

        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            return badRequest("Product not found");
        }

        Product product = productOpt.get();
        Optional<CartItem> existing = cartItemRepository.findByUserIdAndProductId(userId, productId);
        int requestedQuantity = quantity + existing.map(CartItem::getQuantity).orElse(0);
        if (product.getStock() != null && requestedQuantity > product.getStock()) {
            return badRequest("Requested quantity exceeds available stock");
        }

        CartItem cartItem = existing.orElseGet(() -> {
            CartItem item = new CartItem();
            User user = new User();
            user.setId(userId);
            item.setUser(user);
            item.setProduct(product);
            item.setQuantity(0);
            return item;
        });

        cartItem.setQuantity(requestedQuantity);
        cartItemRepository.save(cartItem);

        Map<String, Object> res = new HashMap<>();
        res.put("status", "success");
        res.put("message", "Item added to cart");
        return ResponseEntity.ok(res);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<Map<String, Object>> updateCartItem(@PathVariable Long productId,
            @RequestBody Map<String, Object> request,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return unauthorized();
        }

        int quantity = request.get("quantity") instanceof Number number ? number.intValue() : 1;
        Optional<CartItem> cartItemOpt = cartItemRepository.findByUserIdAndProductId(userId, productId);
        if (cartItemOpt.isEmpty()) {
            return badRequest("Cart item not found");
        }

        CartItem cartItem = cartItemOpt.get();
        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            Integer stock = cartItem.getProduct().getStock();
            if (stock != null && quantity > stock) {
                return badRequest("Requested quantity exceeds available stock");
            }
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("status", "success");
        res.put("message", "Cart updated successfully");
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Map<String, Object>> removeFromCart(@PathVariable Long productId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return unauthorized();
        }

        Optional<CartItem> cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);
        cartItem.ifPresent(cartItemRepository::delete);

        Map<String, Object> res = new HashMap<>();
        res.put("status", "success");
        res.put("message", "Item removed from cart");
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearCart(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return unauthorized();
        }

        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        cartItemRepository.deleteAll(cartItems);

        Map<String, Object> res = new HashMap<>();
        res.put("status", "success");
        res.put("message", "Cart cleared");
        return ResponseEntity.ok(res);
    }

    private ResponseEntity<Map<String, Object>> badRequest(String message) {
        Map<String, Object> res = new HashMap<>();
        res.put("status", "error");
        res.put("message", message);
        return ResponseEntity.badRequest().body(res);
    }

    private ResponseEntity<Map<String, Object>> unauthorized() {
        Map<String, Object> res = new HashMap<>();
        res.put("status", "error");
        res.put("message", "Not logged in");
        return ResponseEntity.status(401).body(res);
    }
}
