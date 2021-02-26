package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.SignupForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    public User getUserByUsername(String username) {
        return userMapper.getUser(username);
    }

    public User getUserByAuthentication(Authentication authentication) {
        return getUserByUsername(authentication.getName());
    }

    public boolean isUsernameAvailable(String username) {
        return getUserByUsername(username) == null;
    }

    public int registerUser(SignupForm signupForm) {
        User user = new User();
        user.setUsername(signupForm.getUsername());
        byte[] salt = hashService.getSalt();
        byte[] password = hashService.computeHash(signupForm.getPassword(), salt);
        user.setSalt(Base64.getEncoder().encodeToString(salt));
        user.setPassword(Base64.getEncoder().encodeToString(password));
        user.setFirstName(signupForm.getFirstName());
        user.setLastName(signupForm.getLastName());
        return userMapper.registerUser(user);
    }
}
