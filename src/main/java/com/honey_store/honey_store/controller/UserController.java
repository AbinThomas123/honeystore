package com.honey_store.honey_store.controller;

import com.honey_store.honey_store.config.JwtUtil;
import com.honey_store.honey_store.dto.UserDTO;
import com.honey_store.honey_store.mapper.UserMapper;
import com.honey_store.honey_store.model.Role;
import com.honey_store.honey_store.model.User;
import com.honey_store.honey_store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // Register user
    @PostMapping
    public ResponseEntity<UserDTO> register(@RequestBody User user) throws Exception {
        User savedUser = userService.register(user);
        return new ResponseEntity<>(UserMapper.toDTO(savedUser), HttpStatus.CREATED);
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        List<UserDTO> users = userService.getAllUsers().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Get user by ID
    @GetMapping("{userId}")
    public ResponseEntity<UserDTO> getUserByUserId(@PathVariable Long userId) throws Exception {
        User user = userService.getUserByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return new ResponseEntity<>(UserMapper.toDTO(user), HttpStatus.OK);
    }

    // Update user fields
    @PatchMapping("{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody Map<String,Object> user) throws Exception {
        User updatedUser = userService.updateUser(userId, user);
        return new ResponseEntity<>(UserMapper.toDTO(updatedUser), HttpStatus.OK);
    }

    // Delete user (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // Update roles (Admin only)
    @PatchMapping("{userId}/roles")
    public ResponseEntity<UserDTO> updateUserRoles(@PathVariable Long userId, @RequestBody Set<String> roles) {
        User updatedUser = userService.updateUserRoles(userId, roles);
        return ResponseEntity.ok(UserMapper.toDTO(updatedUser));
    }

    // Dashboard stats (Admin only)
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getUserStats() {

        return ResponseEntity.ok(userService.getUserStats());
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user){

        if (user == null || user.getUserName() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Username and password are required.");
        }

        User u = userService.loginUser(user);
        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        // Convert Role objects -> role names (e.g. "ADMIN", "CUSTOMER")
        List<String> rolesList = u.getRoles().stream()
                .map(Role::getRoleName)   // ensure Role has getName()
                .toList();

        // Generate JWT with username + roles
        String token = jwtUtil.generateToken(u.getUserName(), rolesList);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("accessToken", token);
        responseBody.put("tokenType", "Bearer");

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", u.getUserId());
        userInfo.put("userName", u.getUserName());
        userInfo.put("email", u.getEmail());
        userInfo.put("roles", rolesList);

        responseBody.put("user", userInfo);

        return ResponseEntity.ok(responseBody);
    }
}
