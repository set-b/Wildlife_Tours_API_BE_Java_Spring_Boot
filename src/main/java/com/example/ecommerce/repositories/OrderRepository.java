package com.example.ecommerce.repositories;

import com.example.ecommerce.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This repository is used for abstracting the storage, retrieval ond miscellaneous behavior for
 * Order Objects.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
