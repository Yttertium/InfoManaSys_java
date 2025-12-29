package com.ynu.service;
import com.ynu.entity.User;

public interface UserService {
    User login(String username, String password);
    void register(User user);
}
