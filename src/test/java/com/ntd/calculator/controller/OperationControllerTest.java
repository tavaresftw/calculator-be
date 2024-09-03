package com.ntd.calculator.controller;

import com.ntd.calculator.data.OperationRequest;
import com.ntd.calculator.data.RecordsResponse;
import com.ntd.calculator.model.enums.OperationType;
import com.ntd.calculator.security.JwtUtil;
import com.ntd.calculator.service.OperationService;
import com.ntd.calculator.service.UserService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

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
    void getRecordsWithFilter() {
        String token = "token";
        String username = "username";

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

        when(userService.getUsernameFromToken(any())).thenReturn(username);

        when(jwtUtil.validateToken(any(), any())).thenReturn(true);
        when(operationService.getRecordsByUserWithFilter(any(), anyInt(), anyInt(), any(), any(), any())).thenReturn(records);
        assertNotNull(operationController.getRecordsWithFilter(token, username, 0, 10, "id", "desc", "search"));
    }

    @Test
    void softDeleteRecord() {
        String token = "token";
        String username = "username";
        Long recordId = 1L;

        when(userService.getUsernameFromToken(token)).thenReturn(username);
        when(jwtUtil.validateToken(token, username)).thenReturn(true);

        assertEquals("Record deleted", operationController.softDeleteRecord(token, recordId).getBody());
    }

    @Test
    void softDeleteRecordFailed() {
        String token = "token";
        String username = "username";
        Long recordId = 1L;

        when(userService.getUsernameFromToken(token)).thenReturn(username);
        when(jwtUtil.validateToken(token, username)).thenReturn(false);
        doNothing().when(operationService).softDeleteRecord(recordId);

        assertEquals("Invalid token", operationController.softDeleteRecord(token, recordId).getBody());
    }

    @Test
    void getRecordsWithFilterFailed() {
        String token = "token";
        String username = "username";

        when(userService.getUsernameFromToken(token)).thenReturn(username);
        when(jwtUtil.validateToken(token, username)).thenReturn(false);

        assertEquals("Invalid token", operationController.getRecordsWithFilter(token, username, 0, 10, "id", "desc", "filter").getBody());
    }

    @Test
    void operationFailed() {
        String token = "token";

        OperationRequest operationRequest = new OperationRequest(
            OperationType.ADDITION,
            new BigDecimal("10.00"),
            new BigDecimal("10.00")
        );

        when(userService.getUsernameFromToken(token)).thenReturn("username");
        when(jwtUtil.validateToken(token, "username")).thenReturn(false);

        assertEquals("Invalid token", operationController.operation(token, operationRequest).getBody());
    }

}