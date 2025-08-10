package com.honey_store.honey_store.service;

import com.honey_store.honey_store.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    Product createProduct(Product product);
}
