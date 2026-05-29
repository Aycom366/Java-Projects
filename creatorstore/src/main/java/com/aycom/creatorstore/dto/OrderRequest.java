package com.aycom.creatorstore.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// creating this coz we don't wanna capture our relation when creating the order
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    @NotNull(message = "Customer name is required")
    private String customerName;

    @NotNull(message = "Customer email is required")
    @Email(message = "Enter a valid email")
    private String customerEmail;

    @JsonBackReference
    @Valid
    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItemRequest> items;

}
