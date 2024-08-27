package com.ntd.calculator.service;

import com.ntd.calculator.data.LoginRequest;
import com.ntd.calculator.data.RegisterRequest;
import com.ntd.calculator.data.UserDTO;
import com.ntd.calculator.model.User;
import com.ntd.calculator.repository.UserRepository;
import com.ntd.calculator.security.JwtUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final BigDecimal BALANCE_DEFAULT = new BigDecimal("10.00");

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostConstruct
    public void insertInitialUsers() {
        registerUser(new RegisterRequest("user1", "password1"));
        registerUser(new RegisterRequest("user2", "password2"));
        registerUser(new RegisterRequest("user3", "password3"));
    }

    public UserDTO registerUser(RegisterRequest userRequest) {
        User user = new User();
        validateUsername(userRequest.getUsername());
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setBalance(BALANCE_DEFAULT);
        user.setStatus("active");
        try {
            User createdUser = userRepository.save(user);
            return convertToUserDTO(createdUser);
        } catch (Exception e) {
            System.out.println("Error");
            throw new RuntimeException("Error creating user");
        }
    }

    public String loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null) {
            System.out.println("User not found");
            throw new RuntimeException("User not found");
        }

        boolean passwordMatch = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        if (!passwordMatch) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getUsername());
    }

    private void validateUsername(String username) {
        if (userRepository.findByUsername(username) != null) {
            throw new RuntimeException("Username already exists");
        }
    }

    public UserDTO getUser(String username) {
        try {
            User user = userRepository.findByUsername(username);

            return convertToUserDTO(user);
        }
        catch (Exception e) {
            System.out.println(e.getCause() + e.getMessage());
            throw new RuntimeException("Error getting user");
        }

    }

    public String getUsernameFromToken(String token) {
        return jwtUtil.extractUsername(token);
    }

    private UserDTO convertToUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getStatus(),
                user.getBalance()
        );
    }
}
