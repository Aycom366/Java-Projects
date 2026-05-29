package com.aycom.creatorstore.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotBlank(message = "customer name is required")
    @Column(nullable = false)
    private String customerName;

    @NotBlank(message = "customer email is required")
    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false, name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonManagedReference // prevent infiinite loop
    // one order can have multiple orderItems
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // you need to define this in the orderItem
    private List<OrderItem> orderItems;

    // run before saving the record to the database
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
