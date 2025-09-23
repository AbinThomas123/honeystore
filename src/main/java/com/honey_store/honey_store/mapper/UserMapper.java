package com.honey_store.honey_store.mapper;

import com.honey_store.honey_store.dto.UserDTO;
import com.honey_store.honey_store.model.Role;
import com.honey_store.honey_store.model.User;

import java.util.stream.Collectors;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getEmail(),
                user.getUserName(),
                user.getGender(),
                user.getRoles().stream()
                        .map(Role::getRoleName)
                        .collect(Collectors.toSet())
        );
    }
}
