package com.ntd.calculator.service;

import com.ntd.calculator.data.LoginRequest;
import com.ntd.calculator.data.RegisterRequest;
import com.ntd.calculator.data.UserDTO;
import com.ntd.calculator.model.User;
import com.ntd.calculator.repository.UserRepository;
import com.ntd.calculator.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final JwtUtil jwtUtil = mock(JwtUtil.class);

    private final UserService userService = new UserService(userRepository, passwordEncoder, jwtUtil);

    @Test
    void registerUserSuccess() {
        User user = new User(
            "username",
            "password",
            "active",
            new BigDecimal("100.00"),
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        RegisterRequest registerRequest = new RegisterRequest("username", "password");

        when(userRepository.save(any())).thenReturn(user);
        when(userRepository.findByUsername(any())).thenReturn(null);
        when(passwordEncoder.encode(any())).thenReturn("password");

        UserDTO userDTO = userService.registerUser(registerRequest);

        assertEquals(user.getUsername(), userDTO.getUsername());
    }

    @Test
    void registerUserFail() {
        User user = new User(
            1L,
            "username",
            "password",
            "active",
            new BigDecimal("100.00"),
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        RegisterRequest registerRequest = new RegisterRequest("username", "password");


        when(userRepository.findByUsername(any())).thenReturn(user);

        assertThrows(RuntimeException.class, () -> userService.registerUser(registerRequest));
    }

    @Test
    void loginUserSuccess() {
        User user = new User(
            1L,
            "username",
            "password",
            "active",
            new BigDecimal("100.00"),
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtUtil.generateToken(any())).thenReturn("token");

        String token = userService.loginUser(new LoginRequest("username", "password"));

        assertEquals("token", token);
    }

    @Test
    void loginUserFail() {
        User user = new User(
            1L,
            "username",
            "password",
            "active",
            new BigDecimal("100.00"),
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.loginUser(new LoginRequest("username", "password")));
    }

    @Test
    void getUserSuccess() {
        User user = new User(
            1L,
            "username",
            "password",
            "active",
            new BigDecimal("100.00"),
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(userRepository.findByUsername(any())).thenReturn(user);

        UserDTO userDTO = userService.getUser("username");

        assertEquals(user.getUsername(), userDTO.getUsername());
    }

    @Test
    void getUserFail() {
        when(userRepository.findByUsername(any())).thenReturn(null);

        assertThrows(RuntimeException.class, () -> userService.getUser("username"));
    }

    @Test
    void addBalanceSuccess() {
        User user = new User(
            "username",
            "password",
            "active",
            new BigDecimal("100.00"),
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);

        UserDTO userDTO = userService.addBalance("username", new BigDecimal("100.00"));

        assertEquals(new BigDecimal("200.00"), userDTO.getBalance());
    }

    @Test
    void addBalanceFail() {
        when(userRepository.findByUsername(any())).thenReturn(null);

        assertThrows(RuntimeException.class, () -> userService.addBalance("username", new BigDecimal("100.00")));
    }

    @Test
    void changePasswordSuccess() {

        User user = new User(
            "username",
            "password",
            "active",
            new BigDecimal("100.00"),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        user.setUsername("username");
        user.setPassword("password");

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);
        when(passwordEncoder.encode(any())).thenReturn("password");

        UserDTO userDTO = userService.changePassword("username", "newpassword");
    }

    @Test
    void changePasswordFail() {
        when(userRepository.findByUsername(any())).thenReturn(null);

        assertThrows(RuntimeException.class, () -> userService.changePassword("username", "newpassword"));
    }

    @Test
    void changeStatusSuccess() {
        User user = new User(
            "username",
            "password",
            "active",
            new BigDecimal("100.00"),
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);

        UserDTO userDTO = userService.changeStatus("username", "inactive");

        assertEquals("inactive", userDTO.getStatus());
    }

    @Test
    void changeStatusFail() {
        when(userRepository.findByUsername(any())).thenReturn(null);

        assertThrows(RuntimeException.class, () -> userService.changeStatus("username", "inactive"));
    }

    @Test
    void getUsernameFromTokenSuccess() {
        when(jwtUtil.extractUsername(any())).thenReturn("username");

        String username = userService.getUsernameFromToken("token");

        assertEquals("username", username);
    }

    @Test
    void getUsernameFromTokenFail() {
        when(jwtUtil.extractUsername(any())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> userService.getUsernameFromToken("token"));
    }

}