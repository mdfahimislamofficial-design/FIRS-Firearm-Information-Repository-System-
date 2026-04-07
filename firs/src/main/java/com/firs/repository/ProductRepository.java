package com.firs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.firs.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByTypeIgnoreCase(String type);

    List<Product> findByManufacturerIgnoreCase(String manufacturer);

    List<Product> findByCaliberIgnoreCase(String caliber);

    Optional<Product> findBySku(String sku);

    List<Product> findByStockLessThan(Integer threshold);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchProducts(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p WHERE p.type = :type AND p.stock > 0 ORDER BY p.price ASC")
    List<Product> findAvailableByType(@Param("type") String type);
}