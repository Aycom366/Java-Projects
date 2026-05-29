package com.aycom.creatorstore.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aycom.creatorstore.entities.Product;

//nothing would be done here, JPA already implemented everything we need
public interface ProductRepository extends JpaRepository<Product, Long> { // entity and unique id of the product

    List<Product> findAllByNameContaining(String p);
}
