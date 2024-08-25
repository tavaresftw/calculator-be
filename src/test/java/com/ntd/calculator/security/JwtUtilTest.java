package com.ntd.calculator.security;

import com.ntd.calculator.service.TokenBlacklistService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtUtilTest {

    private final TokenBlacklistService tokenBlacklistService = mock(TokenBlacklistService.class);
    private final String secretKey = "secret123";
    private final JwtUtil jwtUtil = new JwtUtil(tokenBlacklistService);

    @Test
    void extractUsernameSuccess() {
        String username = "username";

        String token = Jwts.builder()
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String result = jwtUtil.extractUsername(token);
        assertEquals(username, result);
    }

    @Test
    void extractExpirationSuccess() {
        String username = "username";

        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        assertNotNull(jwtUtil.extractExpiration(token));

    }

    @Test
    void extractClaimSuccess() {
        String username = "username";

        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        assertEquals(username, jwtUtil.extractClaim(token, claims -> claims.getSubject()));
    }

    @Test
    void isValidToken() {
        String username = "username";

        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        when(tokenBlacklistService.isTokenBlacklisted(token)).thenReturn(false);

        assertEquals(true, jwtUtil.validateToken(token, username));
    }

    @Test
    void isInvalidToken() {
        String username = "username";

        String token = Jwts.builder()
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        when(tokenBlacklistService.isTokenBlacklisted(token)).thenReturn(true);

        assertEquals(false, jwtUtil.validateToken(token, "invalidUsername"));
    }

    @Test
    void generateTokenSuccess() {
        String username = "username";

        String token = jwtUtil.generateToken(username);

        assertNotNull(token);
    }

}