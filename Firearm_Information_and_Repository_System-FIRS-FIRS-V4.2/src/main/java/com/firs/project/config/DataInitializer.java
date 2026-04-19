package com.firs.project.config;

import com.firs.project.model.Product;
import com.firs.project.model.User;
import com.firs.project.repository.ProductRepository;
import com.firs.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        // Insert default users if none exist
        if (userRepository.count() == 0) {
            List<User> defaultUsers = Arrays.asList(
                createUser("Jason M.", "customer@firs.com", "customer123", "customer"),
                createUser("Lt. James Holloway", "gov@firs.com", "gov123", "govt"),
                createUser("Ridge Arms LLC", "dealer@firs.com", "dealer123", "dealer"),
                createUser("SuperAdmin", "admin@firs.com", "admin123", "admin")
            );
            userRepository.saveAll(defaultUsers);
            System.out.println("✅ Default users inserted.");
        }

        // Insert default products if none exist
        if (productRepository.count() == 0) {
            List<Product> defaultProducts = Arrays.asList(
                createProduct("Glock 19 Gen5", "Glock", "Handgun", 649),
                createProduct("Sig Sauer P320", "Sig Sauer", "Handgun", 629),
                createProduct("M1911", "Colt", "Handgun", 549),
                createProduct("CZ P-10C", "CZ", "Handgun", 499),
                createProduct("Glock 17 Gen5", "Glock", "Handgun", 599),
                createProduct("Sig Sauer P365", "Sig Sauer", "Handgun", 629),
                createProduct("S&W M&P Shield", "Smith & Wesson", "Handgun", 479),
                createProduct("CZ P-07", "CZ", "Handgun", 529),
                createProduct("Ruger GP100", "Ruger", "Revolver", 749),
                createProduct("Colt Python", "Colt", "Revolver", 1499),
                createProduct("Taurus 856", "Taurus", "Revolver", 349),
                createProduct("Charter Arms Bulldog", "Charter Arms", "Revolver", 399),
                createProduct("S&W 686+", "Smith & Wesson", "Revolver", 829),
                createProduct("Ruger SP101", "Ruger", "Revolver", 679),
                createProduct("AR-15 Platform", "Colt", "Rifle", 1299),
                createProduct("M4 Carbine", "Colt", "Rifle", 1499),
                createProduct("AK-47", "Kalashnikov", "Rifle", 899),
                createProduct("Daniel Defense DDM4 V7", "Daniel Defense", "Rifle", 2099),
                createProduct("FN SCAR 17S", "FN Herstal", "Rifle", 3499),
                createProduct("Barrett MRAD Mk22", "Barrett", "Sniper", 5999),
                createProduct("Remington 700", "Remington", "Sniper", 899),
                createProduct("Accuracy International AT", "Accuracy International", "Sniper", 4799),
                createProduct("Savage 110 Elite", "Savage", "Sniper", 1699)
            );
            productRepository.saveAll(defaultProducts);
            System.out.println("✅ Default products inserted.");
        }
    }

    private User createUser(String name, String email, String password, String role) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }

    private Product createProduct(String name, String brand, String category, double price) {
        Product product = new Product();
        product.setName(name);
        product.setBrand(brand);
        product.setCategory(category);
        product.setPrice(price);
        return product;
    }
}