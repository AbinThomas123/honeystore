package com.honey_store.honey_store.dto;

import java.util.Set;

public class UserDTO {

    private Long userId;
    private String email;
    private String userName;
    private String gender;
    private Set<String> roles;


    public UserDTO() {
    }

    // ✅ All-args constructor (useful for mapping)
    public UserDTO(Long userId, String email, String userName, String gender, Set<String> roles) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.gender = gender;
        this.roles = roles;
    }

    // ✅ Getters & Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
