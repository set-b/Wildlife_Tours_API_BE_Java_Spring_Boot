package com.example.ecommerce.repositories;

import com.example.ecommerce.models.Address;
import com.example.ecommerce.models.Customer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This repository is used for abstracting the storage, retrieval ond miscellaneous behavior for
 * Customer Objects.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

  boolean existsByEmail(String email);

  List<Customer> findByAddressIn(List<Address> address);

  Customer findByEmail(String email);
}
