package com.ntd.calculator.controller;

import com.ntd.calculator.data.LoginRequest;
import com.ntd.calculator.data.RegisterRequest;
import com.ntd.calculator.data.UserDTO;
import com.ntd.calculator.service.TokenBlacklistService;
import com.ntd.calculator.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RequestMapping("/user")
@RestController
public class UserController{
    private final UserService userService;
    private final TokenBlacklistService tokenBlacklistService;

    public UserController(UserService userService, TokenBlacklistService tokenBlacklistService) {
        this.userService = userService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            UserDTO createdUserDTO = userService.registerUser(registerRequest);

            return ResponseEntity.ok(createdUserDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            String jwt = userService.loginUser(loginRequest);
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String token) {
        tokenBlacklistService.blacklistToken(token);
        return ResponseEntity.ok("Logout success");
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String username) {
        try {
            UserDTO userDTO = userService.getUser(username);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
