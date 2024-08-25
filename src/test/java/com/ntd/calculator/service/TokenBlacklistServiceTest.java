package com.ntd.calculator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenBlacklistServiceTest {

    private RedisTemplate<String, String> redisTemplate = mock(RedisTemplate.class);

    private ValueOperations<String, String> valueOperations = mock(ValueOperations.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.hasKey("token")).thenReturn(true);
    }

    @Test
    void blacklistToken() {
        TokenBlacklistService tokenBlacklistService = new TokenBlacklistService(redisTemplate);
        tokenBlacklistService.blacklistToken("token");
        assertTrue(tokenBlacklistService.isTokenBlacklisted("token"));
    }

}