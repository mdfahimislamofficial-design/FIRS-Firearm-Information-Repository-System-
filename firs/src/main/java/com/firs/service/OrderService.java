package com.firs.service;

import com.firs.model.CartItem;
import com.firs.model.Order;
import com.firs.model.OrderItem;
import com.firs.model.Product;
import com.firs.model.User;
import com.firs.repository.OrderItemRepository;
import com.firs.repository.OrderRepository;
import com.firs.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order createOrder(Long userId, String shippingAddress, Long fflDealerId) {
        List<CartItem> cartItems = cartService.getCart(userId);
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        Order order = new Order();
        User user = new User();
        user.setId(userId);
        order.setUser(user);
        order.setOrderNumber("ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        order.setShippingAddress(shippingAddress.trim());
        order.setFflDealerId(fflDealerId);
        order.setStatus("PENDING");
        order.setPaymentStatus("PENDING");

        double totalAmount = 0;
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int stock = product.getStock() == null ? 0 : product.getStock();
            if (cartItem.getQuantity() == null || cartItem.getQuantity() <= 0) {
                throw new IllegalArgumentException("Invalid quantity found in cart");
            }
            if (cartItem.getQuantity() > stock) {
                throw new IllegalArgumentException(product.getName() + " does not have enough stock");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            order.getItems().add(orderItem);
            totalAmount += product.getPrice() * cartItem.getQuantity();

            product.setStock(stock - cartItem.getQuantity());
            productRepository.save(product);
        }
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(userId);
        return savedOrder;
    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findUserOrders(userId);
    }

    public Optional<Order> getOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, String status) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            order.get().setStatus(status);
            return orderRepository.save(order.get());
        }
        return null;
    }

    @Transactional
    public Order updatePaymentStatus(Long orderId, String paymentStatus) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            order.get().setPaymentStatus(paymentStatus);
            return orderRepository.save(order.get());
        }
        return null;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
