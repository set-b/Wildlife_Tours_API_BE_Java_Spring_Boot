package com.example.ecommerce.repositories;

import com.example.ecommerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This repository is used for abstracting the storage, retrieval ond miscellaneous behavior for
 * User Objects.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByEmail(String email);

  User findByEmail(String email);
}
