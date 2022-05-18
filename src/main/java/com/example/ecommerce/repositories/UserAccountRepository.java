package com.example.ecommerce.repositories;

import com.example.ecommerce.models.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

  public UserAccount findByUsername(String username);

  boolean existsByUsername(String username);
}
