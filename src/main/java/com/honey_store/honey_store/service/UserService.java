package com.honey_store.honey_store.service;

import com.honey_store.honey_store.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    User register(User user) throws Exception;

    List<User> getAllUsers();

    Optional<User> getUserByUserId(Long userId) throws Exception;

    User updateUser(Long userId, Map<String, Object> user) throws Exception;
    void deleteUser(Long userId);
    User updateUserRoles(Long userId, Set<String> roleNames);
    Map<String, Object> getUserStats();

    User loginUser(User user);
}
