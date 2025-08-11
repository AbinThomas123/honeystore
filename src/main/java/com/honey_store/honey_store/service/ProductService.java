package com.honey_store.honey_store.service;

import com.honey_store.honey_store.model.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<Product> getAllProducts();

    Product addProduct(Product product);

    Product updateProductPartially(Long productId, Map<String, Object> updates);

    Product getProductById(Long productId);

    ResponseEntity<String> deleteProductById(Long productId);
}
