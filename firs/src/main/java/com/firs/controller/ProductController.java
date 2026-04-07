package com.firs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.firs.model.Product;
import com.firs.repository.ProductRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // GET ALL PRODUCTS - SELECT * FROM products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products);
    }

    // GET PRODUCT BY ID - SELECT * FROM products WHERE id = ?
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            return ResponseEntity.ok(Map.of("status", "success", "product", product.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // GET PRODUCTS BY TYPE - SELECT * FROM products WHERE type = ?
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Product>> getProductsByType(@PathVariable String type) {
        List<Product> products = productRepository.findByTypeIgnoreCase(type);
        return ResponseEntity.ok(products);
    }

    // GET PRODUCTS BY MANUFACTURER - SELECT * FROM products WHERE manufacturer = ?
    @GetMapping("/manufacturer/{manufacturer}")
    public ResponseEntity<List<Product>> getProductsByManufacturer(@PathVariable String manufacturer) {
        List<Product> products = productRepository.findByManufacturerIgnoreCase(manufacturer);
        return ResponseEntity.ok(products);
    }

    // SEARCH PRODUCTS - SELECT * FROM products WHERE name LIKE ? OR sku LIKE ?
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productRepository.searchProducts(keyword);
        return ResponseEntity.ok(products);
    }

    // GET LOW STOCK PRODUCTS - SELECT * FROM products WHERE stock < threshold
    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts(@RequestParam(defaultValue = "10") Integer threshold) {
        List<Product> products = productRepository.findByStockLessThan(threshold);
        return ResponseEntity.ok(products);
    }

    // ADD PRODUCT - INSERT INTO products
    @PostMapping
    public ResponseEntity<Map<String, Object>> addProduct(@RequestBody Product product,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        Map<String, Object> res = new HashMap<>();

        // Check if user has dealer or admin role
        if (!"DEALER".equals(role) && !"ADMIN".equals(role)) {
            res.put("status", "error");
            res.put("message", "Access denied. Dealer or Admin role required.");
            return ResponseEntity.status(403).body(res);
        }

        // Validate product data
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            res.put("status", "error");
            res.put("message", "Product name is required");
            return ResponseEntity.badRequest().body(res);
        }

        if (product.getPrice() == null || product.getPrice() <= 0) {
            res.put("status", "error");
            res.put("message", "Valid price is required");
            return ResponseEntity.badRequest().body(res);
        }

        if (product.getType() == null || product.getType().trim().isEmpty()) {
            res.put("status", "error");
            res.put("message", "Product type is required");
            return ResponseEntity.badRequest().body(res);
        }

        // Generate SKU if not provided
        if (product.getSku() == null || product.getSku().trim().isEmpty()) {
            product.setSku(generateSku(product.getName(), product.getType()));
        }

        // INSERT INTO products
        Product saved = productRepository.save(product);

        res.put("status", "success");
        res.put("message", "Product added successfully");
        res.put("product", saved);

        return ResponseEntity.ok(res);
    }

    // UPDATE PRODUCT - UPDATE products SET ... WHERE id = ?
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable Long id,
            @RequestBody Product updated,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        Map<String, Object> res = new HashMap<>();

        if (!"DEALER".equals(role) && !"ADMIN".equals(role)) {
            res.put("status", "error");
            res.put("message", "Access denied. Dealer or Admin role required.");
            return ResponseEntity.status(403).body(res);
        }

        Optional<Product> existing = productRepository.findById(id);

        if (existing.isEmpty()) {
            res.put("status", "error");
            res.put("message", "Product not found");
            return ResponseEntity.notFound().build();
        }

        Product product = existing.get();

        // Update fields
        if (updated.getName() != null)
            product.setName(updated.getName());
        if (updated.getPrice() != null)
            product.setPrice(updated.getPrice());
        if (updated.getImage() != null)
            product.setImage(updated.getImage());
        if (updated.getType() != null)
            product.setType(updated.getType());
        if (updated.getDescription() != null)
            product.setDescription(updated.getDescription());
        if (updated.getStock() != null)
            product.setStock(updated.getStock());
        if (updated.getCaliber() != null)
            product.setCaliber(updated.getCaliber());
        if (updated.getManufacturer() != null)
            product.setManufacturer(updated.getManufacturer());
        if (updated.getSku() != null)
            product.setSku(updated.getSku());

        // UPDATE products SET ... WHERE id = ?
        Product saved = productRepository.save(product);

        res.put("status", "success");
        res.put("message", "Product updated successfully");
        res.put("product", saved);

        return ResponseEntity.ok(res);
    }

    // DELETE PRODUCT - DELETE FROM products WHERE id = ?
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        Map<String, Object> res = new HashMap<>();

        if (!"DEALER".equals(role) && !"ADMIN".equals(role)) {
            res.put("status", "error");
            res.put("message", "Access denied. Dealer or Admin role required.");
            return ResponseEntity.status(403).body(res);
        }

        if (!productRepository.existsById(id)) {
            res.put("status", "error");
            res.put("message", "Product not found");
            return ResponseEntity.notFound().build();
        }

        // DELETE FROM products WHERE id = ?
        productRepository.deleteById(id);

        res.put("status", "success");
        res.put("message", "Product deleted successfully");

        return ResponseEntity.ok(res);
    }

    // Helper method to generate SKU
    private String generateSku(String name, String type) {
        String prefix = type.substring(0, Math.min(3, type.length())).toUpperCase();
        String namePart = name.replaceAll("[^A-Za-z0-9]", "").substring(0, Math.min(5, name.length())).toUpperCase();
        return prefix + "-" + namePart + "-" + System.currentTimeMillis();
    }
}