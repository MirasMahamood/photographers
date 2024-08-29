package com.app.photographers.service.impl;

import com.app.photographers.exception.ErrorMessages;
import com.app.photographers.exception.ResourceAlreadyExistsException;
import com.app.photographers.model.User;
import com.app.photographers.repository.UserRepository;
import com.app.photographers.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void registerUser(User user) {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if (optionalUser.isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } else {
            throw new ResourceAlreadyExistsException(ErrorMessages.ERROR_USER_EXISTS);
        }
    }
}
