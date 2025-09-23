package com.honey_store.honey_store.repository;

import com.honey_store.honey_store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByUserName(String userName);

    User findByUserName(String userName);
}
