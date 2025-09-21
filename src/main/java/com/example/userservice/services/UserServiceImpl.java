package com.example.userservice.services;

import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UerServiceImpl implements UserService{
    private UserRepository userRepository;
    public User
    @Override
    public User signup(String name, String email, String password) {
        return null;
    }

    @Override
    public Token login(String email, String password) {
        return null;
    }

    @Override
    public User validateToken(String tokenValue) {
        return null;
    }
}
