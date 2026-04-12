package com.firs.service;

import com.firs.model.CartItem;
import com.firs.model.Product;
import com.firs.model.User;
import com.firs.repository.CartItemRepository;
import com.firs.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<CartItem> getCart(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    @Transactional
    public CartItem addToCart(Long userId, Long productId, Integer quantity) {
        Optional<CartItem> existing = cartItemRepository.findByUserIdAndProductId(userId, productId);
        Optional<Product> product = productRepository.findById(productId);

        if (product.isEmpty())
            return null;

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + quantity);
            return cartItemRepository.save(existing.get());
        } else {
            CartItem cartItem = new CartItem();
            User user = new User();
            user.setId(userId);
            cartItem.setUser(user);
            cartItem.setProduct(product.get());
            cartItem.setQuantity(quantity);
            return cartItemRepository.save(cartItem);
        }
    }

    @Transactional
    public void updateQuantity(Long userId, Long productId, Integer quantity) {
        Optional<CartItem> cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);
        if (cartItem.isPresent()) {
            if (quantity <= 0) {
                cartItemRepository.delete(cartItem.get());
            } else {
                cartItem.get().setQuantity(quantity);
                cartItemRepository.save(cartItem.get());
            }
        }
    }

    @Transactional
    public void removeFromCart(Long userId, Long productId) {
        Optional<CartItem> cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);
        cartItem.ifPresent(cartItemRepository::delete);
    }

    @Transactional
    public void clearCart(Long userId) {
        cartItemRepository.clearCart(userId);
    }

    public double getCartTotal(Long userId) {
        return getCart(userId).stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }
}