package com.aycom.creatorstore.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aycom.creatorstore.entities.Product;
import com.aycom.creatorstore.services.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    // modifier returnObect method
    public Product createProduct(@Valid @RequestBody Product product) { // validates requestBody object identifier
        return productService.createProduct(product);

    }

    @PutMapping("/{id}")
    public Product UpdateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        return productService.UpdateProduct(id, product);
    }

    @GetMapping
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/search/{productName}")
    public List<Product> findProductByName(@PathVariable String productName) {
        return productService.findProductByName(productName);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(Long id) {
        productService.deleteProduct(id);

    }
}
