package com.example.ecommerce.repositories;

import com.example.ecommerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This repository is used for abstracting the storage, retrieval ond miscellaneous behavior for
 * Product Objects.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  boolean existsBySku(String sku);

  Product findBySku(String sku);
}
