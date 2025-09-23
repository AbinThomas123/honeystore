package com.honey_store.honey_store.serviceImpl;

import com.honey_store.honey_store.model.Role;
import com.honey_store.honey_store.model.User;
import com.honey_store.honey_store.repository.RoleRepository;
import com.honey_store.honey_store.repository.UserRepository;
import com.honey_store.honey_store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) throws Exception {
        if (userRepository.existsByUserName(user.getUserName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        // Hash the password using BCrypt
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // Resolve roles
        user.setRoles(resolveRoles(user.getRoles()));

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserByUserId(Long userId) throws Exception {
        return userRepository.findById(userId)
                .or(() -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Details Not Found for UserID " + userId); });
    }

    @Override
    public User updateUser(Long userId, Map<String, Object> updates) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("UserId Not Found"));

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            switch (key) {
                case "email" -> user.setEmail((String) value);
                case "password" -> user.setPassword(passwordEncoder.encode((String) value));
                case "userName" -> {
                    String newUserName = (String) value;
                    if (userRepository.existsByUserName(newUserName)) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
                    }
                    user.setUserName(newUserName);
                }
                case "dob" -> user.setDob((String) value);
                case "gender" -> user.setGender((String) value);
                default -> throw new IllegalArgumentException("Invalid field: " + key);
            }
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID " + userId);
        }
        userRepository.deleteById(userId);
    }

    @Override
    public User updateUserRoles(Long userId, Set<String> roleNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Set<Role> resolvedRoles = roleNames.stream()
                .map(name -> roleRepository.findByRoleName(name)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + name)))
                .collect(Collectors.toSet());

        user.setRoles(resolvedRoles);
        return userRepository.save(user);
    }

    @Override
    public Map<String, Object> getUserStats() {
        long totalUsers = userRepository.count();
        long admins = userRepository.findAll().stream()
                .filter(u -> u.getRoles().stream().anyMatch(r -> r.getRoleName().equalsIgnoreCase("ADMIN")))
                .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("totalAdmins", admins);
        stats.put("regularUsers", totalUsers - admins);
        return stats;
    }

    @Override
    public User loginUser(User user) {
        User existingUser = userRepository.findByUserName(user.getUserName());

        if (existingUser == null) {
            return null; // user not found
        }

        // Compare hashed password using BCrypt
        if (passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return existingUser;
        } else {
            return null; // wrong password
        }
    }

    private Set<Role> resolveRoles(Set<Role> roles) {
        Set<Role> resolvedRoles = new HashSet<>();
        for (Role r : roles) {
            Role roleFromDb = roleRepository.findByRoleName(r.getRoleName())
                    .orElseThrow(() -> new RuntimeException("Role not found: " + r.getRoleName()));
            resolvedRoles.add(roleFromDb);
        }
        return resolvedRoles;
    }
}
