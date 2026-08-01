package com.learnease.backend.service;

import com.learnease.backend.dto.LoginRequest;
import com.learnease.backend.dto.LoginResponse;
import com.learnease.backend.dto.SignupRequest;
import com.learnease.backend.entity.User;
import com.learnease.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(SignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already exists";
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return "Registration Successful";
    }

    public LoginResponse loginUser(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null) {
            return new LoginResponse(
                    "User not found",
                    null,
                    null
            );
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new LoginResponse(
                    "Invalid Password",
                    null,
                    null
            );
        }

        return new LoginResponse(
                "Login Successful",
                user.getName(),
                user.getEmail()
        );
    }
}