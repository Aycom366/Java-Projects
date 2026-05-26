package com.aycom.creatorstore.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/**
 * @Builder generates a builder pattern for your class.
 *          Instead of:
 * 
 *          Product p = new Product(1L, "Laptop", "Gaming laptop",
 *          "Electronics", new
 *          BigDecimal("999.99"), 50);
 * 
 *          Product p = Product.builder()
 *          .id(1L)
 *          .name("Laptop")
 *          .description("Gaming laptop")
 *          .category("Electronics")
 *          .price(new BigDecimal("999.99"))
 *          .stockQuantity(50)
 *          .build();
 * 
 */
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Column(nullable = false) // required
    private String name;
    private String description;
    private String category;

    @NotNull(message = "price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Column(nullable = false)
    private BigDecimal price;

    @NotNull(message = "stock quantity is required")
    @Min(value = 0, message = "Stock cannot be less than 0") // this is an int, so decimal will not work
    @Column(nullable = false, name = "stock_quantity")
    private Integer stockQuantity;

    @JsonIgnore // I don't want to get data associated with this when i get productById
    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
