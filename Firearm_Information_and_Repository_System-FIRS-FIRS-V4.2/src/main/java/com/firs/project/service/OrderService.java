package com.firs.project.service;

import com.firs.project.model.Order;
import com.firs.project.model.OrderItem;
import com.firs.project.model.Product;
import com.firs.project.model.User;
import com.firs.project.repository.OrderRepository;
import com.firs.project.repository.ProductRepository;
import com.firs.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order createOrder(String userEmail, List<Map<String, Object>> cartItems) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus("Processing");
        double total = 0;

        for (Map<String, Object> itemMap : cartItems) {
            String productName = (String) itemMap.get("name");
            int quantity = (int) itemMap.get("qty");

            // Find product by name (since frontend sends name, not ID)
            List<Product> allProducts = productRepository.findAll();
            Product product = allProducts.stream()
                    .filter(p -> p.getName().equals(productName))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productName));

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setPrice(product.getPrice());
            order.addItem(item);
            total += product.getPrice() * quantity;
        }

        // Add FFL fee ($25 per distinct item)
        total += cartItems.size() * 25;
        order.setTotalAmount(total);

        return orderRepository.save(order);
    }
}