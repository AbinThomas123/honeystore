package com.honey_store.honey_store.repository;

import com.honey_store.honey_store.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository  extends JpaRepository<Product,Long> {
}
