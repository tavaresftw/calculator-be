package com.ntd.calculator.controller;

import com.ntd.calculator.data.OperationRequest;
import com.ntd.calculator.data.RecordsResponse;
import com.ntd.calculator.model.Operation;
import com.ntd.calculator.model.User;
import com.ntd.calculator.model.enums.OperationType;
import com.ntd.calculator.security.JwtUtil;
import com.ntd.calculator.service.OperationService;
import com.ntd.calculator.service.UserService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ntd.calculator.model.Record;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OperationControllerTest {
    private final OperationService operationService = mock(OperationService.class);
    private final UserService userService = mock(UserService.class);
    private final JwtUtil jwtUtil = mock(JwtUtil.class);

    private final OperationController operationController = new OperationController(operationService, userService, jwtUtil);

    @Test
    void operationSuccess() {
        String token = "token";
        OperationRequest operationRequest = new OperationRequest(
            OperationType.ADDITION,
            new BigDecimal("10.00"),
            new BigDecimal("10.00")
        );

        when(userService.getUsernameFromToken(token)).thenReturn("username");
        when(jwtUtil.validateToken(token, "username")).thenReturn(true);
        when(operationService.executeOperation("username", new BigDecimal("10.00"), new BigDecimal("10.00"), OperationType.ADDITION)).thenReturn("20.00");

        String result = operationController.operation(token, operationRequest).getBody();
        assertEquals("20.00", result);

    }

    @Test
    void getRecords() {
        String token = "token";
        String username = "username";

        Operation operation = new Operation(
            1L,
            OperationType.ADDITION,
            new BigDecimal("10.00")
        );

        User user = new User(
            "username",
            "password",
            "active",
            new BigDecimal("100.00"),
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        List<RecordsResponse> records = List.of(
            new RecordsResponse(
                1L,
                "user",
                new BigDecimal("10.00"),
                "10.00",
                "success",
                new BigDecimal("110.00")
            )
        );

        when(userService.getUsernameFromToken(token)).thenReturn(username);

        when(jwtUtil.validateToken(token, username)).thenReturn(true);
        when(operationService.getRecordsByUser(username)).thenReturn(records);

        assertNotNull(operationController.getRecords(token, username).getBody());

    }

}