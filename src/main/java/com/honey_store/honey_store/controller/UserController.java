package com.honey_store.honey_store.controller;


import com.honey_store.honey_store.model.User;
import com.honey_store.honey_store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> register(@RequestBody User user) throws Exception {

        User savedUser = userService.register(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);

    }
    @GetMapping
    public ResponseEntity<List<User>> getAll()
    {
        List<User> users=userService.getAllUsers();
        return new ResponseEntity<>(users,HttpStatus.FOUND);
    }
    @GetMapping("{userId}")

    public ResponseEntity<Optional<User>> getUserByUserId(@PathVariable Long userId) throws Exception {
        Optional<User> user = userService.getUserByUserId(userId);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }
    @PatchMapping("{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId,@RequestBody Map<String,Object> user) throws Exception {
        User u=userService.updateUser(userId,user);
        return new ResponseEntity<>(u,HttpStatus.OK);
    }
}
