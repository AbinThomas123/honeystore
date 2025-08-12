package com.honey_store.honey_store.serviceImpl;


import com.honey_store.honey_store.config.EncryptDecrypt;
import com.honey_store.honey_store.model.Role;
import com.honey_store.honey_store.model.User;
import com.honey_store.honey_store.repository.RoleRepository;
import com.honey_store.honey_store.repository.UserRepository;
import com.honey_store.honey_store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private final EncryptDecrypt aesEncrypt;

    public UserServiceImpl(EncryptDecrypt encrypt, EncryptDecrypt aesEncrypt) {
        this.aesEncrypt = aesEncrypt;
    }


    @Override
    public User register(User user) throws Exception {

        String encryptedPassword = aesEncrypt.encrypt(user.getPassword());
        user.setPassword(encryptedPassword);

        if (userRepository.existsByUserName(user.getUserName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        Set<Role> resolvedRoles = new HashSet<>();
        for (Role r : user.getRoles()) {
            Role roleFromDb = (Role) roleRepository.findByRoleName(r.getRoleName())
                    .orElseThrow(() -> new RuntimeException("Role not found: " + r.getRoleName()));
            resolvedRoles.add(roleFromDb);
        }
        user.setRoles(resolvedRoles);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserByUserId(Long userId) throws Exception {

        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent())
        {
            return user;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Details Not Found for the  UserID "+userId);
    }
}
