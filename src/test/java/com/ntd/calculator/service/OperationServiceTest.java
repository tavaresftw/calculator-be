package com.ntd.calculator.service;

import com.ntd.calculator.Client.RandomStringClient;
import com.ntd.calculator.model.Operation;
import com.ntd.calculator.model.Record;
import com.ntd.calculator.model.User;
import com.ntd.calculator.model.enums.OperationType;
import com.ntd.calculator.repository.OperationRepository;
import com.ntd.calculator.repository.RecordRepository;
import com.ntd.calculator.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.aot.hint.TypeReference.listOf;

class OperationServiceTest {

    private final OperationRepository operationRepository = mock(OperationRepository.class);
    private final RecordRepository recordRepository = mock(RecordRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final RandomStringClient randomStringClient = mock(RandomStringClient.class);

    private final OperationService operationService = new OperationService(operationRepository, recordRepository, userRepository, randomStringClient);

    @Test
    void additionTest() {
        String username = "teste";
        BigDecimal a = new BigDecimal("10.00");
        BigDecimal b = new BigDecimal("20.00");
        BigDecimal expected = new BigDecimal("30.00");
        User user = new User();
        user.setUsername(username);
        user.setBalance(BigDecimal.valueOf(200));
        OperationType operationType = OperationType.ADDITION;

        Operation operation = new Operation();
        operation.setType(OperationType.ADDITION);
        operation.setCost(BigDecimal.valueOf(10));

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(operationRepository.findByType(any())).thenReturn(operation);

        String result = operationService.executeOperation(username, a, b, operationType);

        assertEquals(expected.toString(), result);
    }

    @Test
    void subtractionTest() {
        String username = "teste";
        BigDecimal a = new BigDecimal("20.00");
        BigDecimal b = new BigDecimal("10.00");
        BigDecimal expected = new BigDecimal("10.00");
        User user = new User();
        user.setUsername(username);
        user.setBalance(BigDecimal.valueOf(200));
        OperationType operationType = OperationType.SUBTRACTION;

        Operation operation = new Operation();
        operation.setType(OperationType.SUBTRACTION);
        operation.setCost(BigDecimal.valueOf(10));

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(operationRepository.findByType(any())).thenReturn(operation);

        String result = operationService.executeOperation(username, a, b, operationType);

        assertEquals(expected.toString(), result);
    }

    @Test
    void multiplicationTest() {
        String username = "teste";
        BigDecimal a = new BigDecimal("10.00");
        BigDecimal b = new BigDecimal("20.00");
        BigDecimal expected = new BigDecimal("200.00");
        User user = new User();
        user.setUsername(username);
        user.setBalance(BigDecimal.valueOf(200));
        OperationType operationType = OperationType.MULTIPLICATION;

        Operation operation = new Operation();
        operation.setType(OperationType.MULTIPLICATION);
        operation.setCost(BigDecimal.valueOf(10));

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(operationRepository.findByType(any())).thenReturn(operation);

        String result = operationService.executeOperation(username, a, b, operationType);

        assertEquals(expected.toString(), result);
    }

    @Test
    void divisionTest() {
        String username = "teste";
        BigDecimal a = new BigDecimal("20.00");
        BigDecimal b = new BigDecimal("10.00");
        BigDecimal expected = new BigDecimal("2.00");
        User user = new User();
        user.setUsername(username);
        user.setBalance(BigDecimal.valueOf(200));
        OperationType operationType = OperationType.DIVISION;

        Operation operation = new Operation();
        operation.setType(OperationType.DIVISION);
        operation.setCost(BigDecimal.valueOf(10));

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(operationRepository.findByType(any())).thenReturn(operation);

        String result = operationService.executeOperation(username, a, b, operationType);

        assertEquals(expected.toString(), result);
    }

    @Test
    void squareRootTest() {
        String username = "teste";
        BigDecimal a = new BigDecimal("25.00");
        BigDecimal expected = new BigDecimal("5.00");
        User user = new User();
        user.setUsername(username);
        user.setBalance(BigDecimal.valueOf(200));
        OperationType operationType = OperationType.SQUARE_ROOT;

        Operation operation = new Operation();
        operation.setType(OperationType.SQUARE_ROOT);
        operation.setCost(BigDecimal.valueOf(10));

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(operationRepository.findByType(any())).thenReturn(operation);

        String result = operationService.executeOperation(username, a, null, operationType);

        assertEquals(expected.toString(), result);
    }

    @Test
    void randomStringTest() {
        String username = "teste";
        BigDecimal a = new BigDecimal("10.00");
        String expected = "random";
        User user = new User();
        user.setUsername(username);
        user.setBalance(BigDecimal.valueOf(200));
        OperationType operationType = OperationType.RANDOM_STRING;

        Operation operation = new Operation();
        operation.setType(OperationType.RANDOM_STRING);
        operation.setCost(BigDecimal.valueOf(10));

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(operationRepository.findByType(any())).thenReturn(operation);
        when(randomStringClient.getRandomString(any())).thenReturn(expected);

        String result = operationService.executeOperation(username, a, null, operationType);

        assertEquals(expected, result);
    }

    @Test
    void insufficientBalanceTest() {
        String username = "teste";
        BigDecimal a = new BigDecimal("10.00");
        BigDecimal b = new BigDecimal("20.00");
        User user = new User();
        user.setUsername(username);
        user.setBalance(BigDecimal.valueOf(5));
        OperationType operationType = OperationType.ADDITION;

        Operation operation = new Operation();
        operation.setType(OperationType.ADDITION);
        operation.setCost(BigDecimal.valueOf(10));

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(operationRepository.findByType(any())).thenReturn(operation);

        assertThrows(RuntimeException.class, () -> operationService.executeOperation(username, a, b, operationType));
    }

    @Test
    void invalidOperationTypeTest() {
        String username = "teste";
        BigDecimal a = new BigDecimal("10.00");
        BigDecimal b = new BigDecimal("00.00");
        User user = new User();
        user.setUsername(username);
        user.setBalance(BigDecimal.valueOf(200));
        OperationType operationType = OperationType.DIVISION;

        Operation operation = new Operation();
        operation.setType(OperationType.DIVISION);
        operation.setCost(BigDecimal.valueOf(10));

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(operationRepository.findByType(any())).thenReturn(operation);

        assertThrows(RuntimeException.class, () -> operationService.executeOperation(username, a, b, operationType));
    }

    @Test
    void invalidOperationSquareRootTest() {
        String username = "teste";
        BigDecimal a = new BigDecimal("-10.00");
        User user = new User();
        user.setUsername(username);
        user.setBalance(BigDecimal.valueOf(200));
        OperationType operationType = OperationType.SQUARE_ROOT;

        Operation operation = new Operation();
        operation.setType(OperationType.SQUARE_ROOT);
        operation.setCost(BigDecimal.valueOf(10));

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(operationRepository.findByType(any())).thenReturn(operation);

        assertThrows(RuntimeException.class, () -> operationService.executeOperation(username, a, null, operationType));
    }

    @Test
    void invalidOperationRandomStringTest() {
        String username = "teste";
        BigDecimal a = new BigDecimal("10.00");
        User user = new User();
        user.setUsername(username);
        user.setBalance(BigDecimal.valueOf(200));
        OperationType operationType = OperationType.RANDOM_STRING;

        Operation operation = new Operation();
        operation.setType(OperationType.RANDOM_STRING);
        operation.setCost(BigDecimal.valueOf(10));

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(operationRepository.findByType(any())).thenReturn(operation);

        assertThrows(RuntimeException.class, () -> operationService.executeOperation(username, a, null, operationType));
    }

    @Test
    void getRecordTest() {
        String username = "teste";
        User user = new User();
        List<Record> expected = List.of(new Record(
                1L, new Operation(), user, new BigDecimal("10.00"), new BigDecimal("20.00"), "result", LocalDateTime.now()
        ));
        when(userRepository.findByUsername(any())).thenReturn(user);
        when(recordRepository.findByUser(any())).thenReturn(expected);

        List<Record> result = operationService.getRecordsByUser(username);
        Assertions.assertNotEquals(List.of(), result);
    }

    @Test
    void getRecordFailedTest() {
        String username = "teste";
        User user = new User();
        when(userRepository.findByUsername(any())).thenReturn(user);
        when(recordRepository.findByUser(any())).thenReturn(List.of());

        List<Record> result = operationService.getRecordsByUser(username);
        Assertions.assertEquals(List.of(), result);
    }

    @Test
    void getRecordExceptionTest() {
        String username = "teste";
        User user = new User();
        when(userRepository.findByUsername(any())).thenReturn(user);
        when(recordRepository.findByUser(any())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> operationService.getRecordsByUser(username));
    }
}