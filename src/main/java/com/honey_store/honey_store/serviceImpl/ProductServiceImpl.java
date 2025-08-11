package com.honey_store.honey_store.serviceImpl;


import com.honey_store.honey_store.model.Product;
import com.honey_store.honey_store.repository.ProductRepository;
import com.honey_store.honey_store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product addProduct(Product product) {

        return productRepository.save(product);
    }

    @Override
    public Product updateProductPartially(Long productId, Map<String, Object> updates) {
       Product product=productRepository.findById(productId).orElseThrow(()->new RuntimeException("Product Not Found"));

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            switch (key) {
                case "name" -> product.setName((String) value);
                case "price" -> product.setPrice(Double.parseDouble(value.toString()));
                case "stock" -> product.setStock(Integer.parseInt(value.toString()));
                case "description" -> product.setDescription((String) value);
                default -> throw new IllegalArgumentException("Invalid field: " + key);
            }
        }
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long productId) {
        Product product=productRepository.findById(productId).orElseThrow(()->new RuntimeException("Product Not Found for this ProductID"+productId));

        return product;

    }

    @Override
    public ResponseEntity<String> deleteProductById(Long productId) {

        Product product=productRepository.findById(productId).orElseThrow(()->new RuntimeException("Product Not Found for this ProductID"+productId));

            productRepository.deleteById(productId);

            return ResponseEntity.ok("Product Deleted Successfully " +productId);

    }
}
