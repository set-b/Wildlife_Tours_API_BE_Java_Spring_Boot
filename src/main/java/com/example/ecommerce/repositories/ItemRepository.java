package com.example.ecommerce.repositories;

import com.example.ecommerce.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This repository is used for abstracting the storage, retrieval ond miscellaneous behavior for
 * Item Objects.
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}
