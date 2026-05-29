package com.aycom.creatorstore.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;

import com.aycom.creatorstore.dto.OrderItemRequest;
import com.aycom.creatorstore.dto.OrderRequest;
import com.aycom.creatorstore.entities.Order;
import com.aycom.creatorstore.entities.OrderItem;
import com.aycom.creatorstore.entities.Product;
import com.aycom.creatorstore.repositories.OrderRepository;
import com.aycom.creatorstore.repositories.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private OrderRepository orderRepository;
    private ProductRepository productRepository;

    @Transactional
    public Order createOrder(OrderRequest orderRequest) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        Order order = new Order();
        order.setCustomerEmail(orderRequest.getCustomerEmail());
        order.setCustomerName(orderRequest.getCustomerName());
        order.setStatus("CONFIRMED");

        // loop through order request items and see if product actually exists
        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id " + itemRequest.getProductId()));

            // check the product stock
            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new RuntimeErrorException(null, "Not enough stock for this particular " + product.getName());
            }

            // calculate total price
            BigDecimal priceOfItem = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            totalPrice = totalPrice.add(priceOfItem);

            // update the product table with latest stock quantity
            product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
            productRepository.save(product);

            // Builder Pattern to make object
            OrderItem orderItem = OrderItem.builder().order(order).product(product).quantity(itemRequest.getQuantity())
                    .priceAtPurchase(product.getPrice()).build();

            orderItems.add(orderItem);
        }

        order.setTotalPrice(totalPrice);
        order.setOrderItems(orderItems);

        return orderRepository.save(order);
    }
}
