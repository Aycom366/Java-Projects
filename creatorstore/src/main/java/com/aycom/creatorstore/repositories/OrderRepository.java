package com.aycom.creatorstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aycom.creatorstore.entities.Order;

//nothing would be done here, JPA already implemented everything we need
public interface OrderRepository extends JpaRepository<Order, Long> { // entity and unique id of the product

}
