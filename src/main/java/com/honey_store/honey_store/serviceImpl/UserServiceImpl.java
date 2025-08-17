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

import java.util.*;
import java.util.stream.Collectors;

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
        // use helper method
        user.setRoles(resolveRoles(user.getRoles()));
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

    @Override
    public User updateUser(Long userId, Map<String, Object> user) throws Exception {
        User u=userRepository.findById(userId).orElseThrow(()->new RuntimeException("UserId Not Found"));

        for(Map.Entry<String,Object> entry:user.entrySet())
        {
        String key=entry.getKey();
        Object value=entry.getValue();
        switch (key)
        {
            case "email"->u.setEmail((String)value);
            case "password"->u.setPassword(aesEncrypt.encrypt((String)value));
            case "userName" -> {
                String newUserName = (String) value;
                if (userRepository.existsByUserName(newUserName)) {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT, "Username already exists");
                }
                u.setUserName(newUserName);
            }
                case "dob" -> u.setDob((String) value);
                case "gender" -> u.setGender((String) value);
                default -> throw new IllegalArgumentException("Invalid field: " + key);
            }
        }
        return userRepository.save(u);
    }


    private Set<Role> resolveRoles(Set<Role> roles) {
        Set<Role> resolvedRoles = new HashSet<>();
        for (Role r : roles) {
            Role roleFromDb = (Role) roleRepository.findByRoleName(r.getRoleName())
                    .orElseThrow(() -> new RuntimeException("Role not found: " + r.getRoleName()));
            resolvedRoles.add(roleFromDb);
        }
        return resolvedRoles;
    }


}
