package com.aycom.creatorstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aycom.creatorstore.entities.OrderItem;

//nothing would be done here, JPA already implemented everything we need
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> { // entity and unique id of the product

}
