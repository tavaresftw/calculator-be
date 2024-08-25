package com.ntd.calculator.controller;

import com.ntd.calculator.data.LoginRequest;
import com.ntd.calculator.data.RegisterRequest;
import com.ntd.calculator.data.UserDTO;
import com.ntd.calculator.service.TokenBlacklistService;
import com.ntd.calculator.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {
    private final UserService userService = mock(UserService.class);
    private final TokenBlacklistService tokenBlacklistService = mock(TokenBlacklistService.class);
    private final UserController userController = new UserController(userService, tokenBlacklistService);

    @Test
    void registerUserSuccess() {
        String username = "username";
        String password = "password";
        UserDTO userDto = new UserDTO(
                1L,
                username,
                "active",
                new BigDecimal("100.00")
        );

        when(userService.registerUser(any())).thenReturn(userDto);

        ResponseEntity<UserDTO> response = userController.registerUser(new RegisterRequest(username, password));

        assertEquals(username, response.getBody().getUsername());
    }

    @Test
    void registerUserFail() {
        String username = "username";
        String password = "password";

        when(userService.registerUser(any())).thenThrow(new RuntimeException());

        ResponseEntity<UserDTO> response = userController.registerUser(new RegisterRequest(username, password));

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void getUserSuccess() {
        String username = "username";
        UserDTO userDto = new UserDTO(
                1L,
                username,
                "active",
                new BigDecimal("100.00")
        );

        when(userService.getUser(any())).thenReturn(userDto);

        ResponseEntity<UserDTO> response = userController.getUser(username);

        assertEquals(username, response.getBody().getUsername());
    }

    @Test
    void getUserFail() {
        String username = "username";

        when(userService.getUser(any())).thenThrow(new RuntimeException());

        ResponseEntity<UserDTO> response = userController.getUser(username);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void addBalanceSuccess() {
        String username = "username";
        BigDecimal amount = new BigDecimal("100.00");
        UserDTO userDto = new UserDTO(
                1L,
                username,
                "active",
                new BigDecimal("200.00")
        );

        when(userService.addBalance(any(), any())).thenReturn(userDto);

        ResponseEntity<UserDTO> response = userController.addBalance(username, amount);

        assertEquals(new BigDecimal("200.00"), response.getBody().getBalance());
    }

    @Test
    void addBalanceFail() {
        String username = "username";
        BigDecimal amount = new BigDecimal("100.00");

        when(userService.addBalance(any(), any())).thenThrow(new RuntimeException());

        ResponseEntity<UserDTO> response = userController.addBalance(username, amount);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void changeStatusSuccess() {
        String username = "username";
        String status = "active";
        UserDTO userDto = new UserDTO(
                1L,
                username,
                status,
                new BigDecimal("100.00")
        );

        when(userService.changeStatus(any(), any())).thenReturn(userDto);

        ResponseEntity<UserDTO> response = userController.changeStatus(username, status);

        assertEquals(status, response.getBody().getStatus());
    }

    @Test
    void changeStatusFail() {
        String username = "username";
        String status = "active";

        when(userService.changeStatus(any(), any())).thenThrow(new RuntimeException());

        ResponseEntity<UserDTO> response = userController.changeStatus(username, status);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void loginUserSuccess() {
        LoginRequest loginRequest = new LoginRequest("username", "password");

        when(userService.loginUser(any())).thenReturn("token");

        ResponseEntity<String> response = userController.loginUser(loginRequest);

        assertEquals("token", response.getBody());
    }

    @Test
    void loginUserFail() {
        LoginRequest loginRequest = new LoginRequest("username", "password");

        when(userService.loginUser(any())).thenThrow(new RuntimeException());

        ResponseEntity<String> response = userController.loginUser(loginRequest);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void logoutUserSuccess() {
        String token = "token";
        tokenBlacklistService.blacklistToken(any());

        ResponseEntity<String> response = userController.logoutUser(token);
        assertEquals("Logout success", response.getBody());
    }

}