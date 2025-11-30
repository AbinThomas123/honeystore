package com.honey_store.honey_store.controller;


import com.honey_store.honey_store.entity.ErrorResponse;
import com.honey_store.honey_store.exceptions.ProductNotFoundException;
import com.honey_store.honey_store.model.Product;
import com.honey_store.honey_store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product)
    {
        Product products=productService.addProduct(product);
        return new ResponseEntity<>(products, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts()
    {
        List<Product> products=productService.getAllProducts();
    return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId)
    {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<Product>  updateProduct(@RequestBody Map<String,Object> updates, @PathVariable Long productId)
    {
        Product updatedProduct = productService.updateProductPartially(productId, updates);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId)
    {
       return productService.deleteProductById(productId);
    }







}
