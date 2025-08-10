package com.honey_store.honey_store.controller;


import com.honey_store.honey_store.model.Product;
import com.honey_store.honey_store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/createProduct")
    public ResponseEntity<Product> createProduct(@RequestBody Product product)
    {
        Product products=productService.createProduct(product);
        return new ResponseEntity<>(products, HttpStatus.CREATED);
    }


    @GetMapping("/getAll")
    public ResponseEntity<List<Product>> getAllProducts()
    {
        List<Product> products=productService.getAllProducts();
    return new ResponseEntity<>(products, HttpStatus.OK);
    }





}
