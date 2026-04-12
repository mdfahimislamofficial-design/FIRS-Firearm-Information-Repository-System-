package com.firs.controller;

import com.firs.model.Product;
import com.firs.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String manufacturer,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String sortBy) {

        List<Product> products = productRepository.findAll();

        if (keyword != null && !keyword.isBlank()) {
            String q = keyword.toLowerCase(Locale.ROOT);
            products = products.stream().filter(product -> contains(product.getName(), q)
                    || contains(product.getSku(), q)
                    || contains(product.getType(), q)
                    || contains(product.getManufacturer(), q)
                    || contains(product.getCaliber(), q)).collect(Collectors.toList());
        }

        if (type != null && !type.isBlank()) {
            products = products.stream().filter(p -> type.equalsIgnoreCase(p.getType())).collect(Collectors.toList());
        }

        if (manufacturer != null && !manufacturer.isBlank()) {
            products = products.stream().filter(p -> manufacturer.equalsIgnoreCase(p.getManufacturer()))
                    .collect(Collectors.toList());
        }

        if (minPrice != null) {
            products = products.stream().filter(p -> p.getPrice() != null && p.getPrice() >= minPrice)
                    .collect(Collectors.toList());
        }

        if (maxPrice != null) {
            products = products.stream().filter(p -> p.getPrice() != null && p.getPrice() <= maxPrice)
                    .collect(Collectors.toList());
        }

        if (sortBy != null) {
            switch (sortBy.toLowerCase(Locale.ROOT)) {
                case "price_asc" -> products.sort(Comparator.comparing(Product::getPrice, Comparator.nullsLast(Double::compareTo)));
                case "price_desc" -> products.sort(Comparator.comparing(Product::getPrice, Comparator.nullsLast(Double::compareTo)).reversed());
                case "stock_desc" -> products.sort(Comparator.comparing(Product::getStock, Comparator.nullsLast(Integer::compareTo)).reversed());
                default -> products.sort(Comparator.comparing(Product::getName, Comparator.nullsLast(String::compareToIgnoreCase)));
            }
        }

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            Map<String, Object> res = new HashMap<>();
            res.put("status", "success");
            res.put("product", product.get());
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Product>> getProductsByType(@PathVariable String type) {
        return ResponseEntity.ok(productRepository.findByTypeIgnoreCase(type));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(productRepository.searchProducts(keyword));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts(@RequestParam(defaultValue = "10") Integer threshold) {
        return ResponseEntity.ok(productRepository.findByStockLessThan(threshold));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createProduct(@RequestBody Product product, HttpSession session) {
        if (!hasRole(session, "ADMIN") && !hasRole(session, "DEALER")) {
            return unauthorized("Only admin or dealer can add firearms");
        }

        Map<String, Object> validation = validateProduct(product);
        if (validation != null) {
            return ResponseEntity.badRequest().body(validation);
        }

        product.setType(product.getType().toUpperCase(Locale.ROOT));
        Product savedProduct = productRepository.save(product);

        Map<String, Object> res = new HashMap<>();
        res.put("status", "success");
        res.put("message", "Firearm listing created successfully");
        res.put("product", savedProduct);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct,
            HttpSession session) {
        if (!hasRole(session, "ADMIN") && !hasRole(session, "DEALER")) {
            return unauthorized("Only admin or dealer can update firearms");
        }

        Optional<Product> existingOpt = productRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Product product = existingOpt.get();
        if (updatedProduct.getName() != null && !updatedProduct.getName().isBlank()) product.setName(updatedProduct.getName().trim());
        if (updatedProduct.getSku() != null && !updatedProduct.getSku().isBlank()) product.setSku(updatedProduct.getSku().trim());
        if (updatedProduct.getType() != null && !updatedProduct.getType().isBlank()) product.setType(updatedProduct.getType().trim().toUpperCase(Locale.ROOT));
        if (updatedProduct.getManufacturer() != null) product.setManufacturer(updatedProduct.getManufacturer().trim());
        if (updatedProduct.getCaliber() != null) product.setCaliber(updatedProduct.getCaliber().trim());
        if (updatedProduct.getDescription() != null) product.setDescription(updatedProduct.getDescription().trim());
        if (updatedProduct.getImage() != null) product.setImage(updatedProduct.getImage().trim());
        if (updatedProduct.getPrice() != null) product.setPrice(updatedProduct.getPrice());
        if (updatedProduct.getStock() != null) product.setStock(updatedProduct.getStock());

        Product saved = productRepository.save(product);
        Map<String, Object> res = new HashMap<>();
        res.put("status", "success");
        res.put("message", "Firearm listing updated successfully");
        res.put("product", saved);
        return ResponseEntity.ok(res);
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(keyword);
    }

    private boolean hasRole(HttpSession session, String role) {
        Object sessionRole = session.getAttribute("userRole");
        return sessionRole != null && role.equalsIgnoreCase(String.valueOf(sessionRole));
    }

    private ResponseEntity<Map<String, Object>> unauthorized(String message) {
        Map<String, Object> res = new HashMap<>();
        res.put("status", "error");
        res.put("message", message);
        return ResponseEntity.status(401).body(res);
    }

    private Map<String, Object> validateProduct(Product product) {
        Map<String, Object> res = new HashMap<>();
        if (product.getName() == null || product.getName().isBlank()) {
            res.put("status", "error");
            res.put("message", "Firearm name is required");
            return res;
        }
        if (product.getType() == null || product.getType().isBlank()) {
            res.put("status", "error");
            res.put("message", "Firearm type is required");
            return res;
        }
        if (product.getPrice() == null || product.getPrice() < 0) {
            res.put("status", "error");
            res.put("message", "Valid price is required");
            return res;
        }
        if (product.getStock() != null && product.getStock() < 0) {
            res.put("status", "error");
            res.put("message", "Stock cannot be negative");
            return res;
        }
        return null;
    }
}
