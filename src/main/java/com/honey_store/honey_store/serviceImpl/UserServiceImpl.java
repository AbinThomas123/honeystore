package com.honey_store.honey_store.serviceImpl;


import com.honey_store.honey_store.config.EncryptDecrypt;
import com.honey_store.honey_store.model.User;
import com.honey_store.honey_store.repository.UserRepository;
import com.honey_store.honey_store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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

        return userRepository.save(user);

    }
}
