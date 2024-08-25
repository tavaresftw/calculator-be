package com.ntd.calculator.service;

import com.ntd.calculator.data.LoginRequest;
import com.ntd.calculator.data.RegisterRequest;
import com.ntd.calculator.data.UserDTO;
import com.ntd.calculator.model.User;
import com.ntd.calculator.repository.UserRepository;
import com.ntd.calculator.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final BigDecimal BALANCE_DEFAULT = new BigDecimal("100.00");

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    public UserDTO registerUser(RegisterRequest userRequest) {
        User user = new User();
        validateUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setBalance(BALANCE_DEFAULT);
        user.setStatus("active");

        User createdUser = userRepository.save(user);
        return convertToUserDTO(createdUser);
    }

    public String loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null) {
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
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return convertToUserDTO(user);
    }

    public UserDTO addBalance(String username, BigDecimal amount) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        user.setBalance(user.getBalance().add(amount));
        User updatedUser = userRepository.save(user);
        return convertToUserDTO(updatedUser);
    }

    public UserDTO changeStatus(String username, String status) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        user.setStatus(status);
        User updatedUser = userRepository.save(user);
        return convertToUserDTO(updatedUser);
    }

    public UserDTO changePassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        User updatedUser = userRepository.save(user);
        return convertToUserDTO(updatedUser);
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
