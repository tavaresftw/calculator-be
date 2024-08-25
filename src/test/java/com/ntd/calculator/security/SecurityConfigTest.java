package com.ntd.calculator.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    private final SecurityConfig securityConfig = new SecurityConfig();

    @Test
    void passwordEncoder() {
        String password = "password";
        String encodedPassword = securityConfig.passwordEncoder().encode(password);
        assertTrue(securityConfig.passwordEncoder().matches(password, encodedPassword));
    }
}