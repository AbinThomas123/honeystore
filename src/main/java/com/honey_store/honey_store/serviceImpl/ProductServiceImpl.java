package com.honey_store.honey_store.serviceImpl;


import com.honey_store.honey_store.model.Product;
import com.honey_store.honey_store.repository.ProductRepository;
import com.honey_store.honey_store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(Product product) {

        return productRepository.save(product);
    }
}
