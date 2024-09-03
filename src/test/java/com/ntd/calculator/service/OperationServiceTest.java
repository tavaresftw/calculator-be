package com.ntd.calculator.service;

import com.ntd.calculator.client.RandomStringClient;
import com.ntd.calculator.data.RecordsResponse;
import com.ntd.calculator.model.Operation;
import com.ntd.calculator.model.Record;
import com.ntd.calculator.model.User;
import com.ntd.calculator.model.enums.OperationType;
import com.ntd.calculator.repository.OperationRepository;
import com.ntd.calculator.repository.RecordRepository;
import com.ntd.calculator.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    void getRecordWithFilterTest() {
        String username = "teste";
        User user = new User();
        String sortBy = "id";
        Sort.Direction direction = Sort.Direction.DESC;
        String search = "search";

        Record record = new Record(
                1L, new Operation(1L, OperationType.ADDITION, new BigDecimal("10.00")), user, new BigDecimal("10.00"), new BigDecimal("20.00"), "result", LocalDateTime.now(), false
        );
        Page<Record> expected = new PageImpl<>(List.of(record));

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(recordRepository.findRecordByUserIdAndFilter(any(), any(), any())).thenReturn(expected);

        List<RecordsResponse> result = operationService.getRecordsByUserWithFilter(username, 1, 1, sortBy, direction, search);
        Assertions.assertNotEquals(List.of(), result);
    }

    @Test
    void getRecordWithFilterExceptionTest() {
        String username = "teste";
        User user = new User();
        String sortBy = "id";
        Sort.Direction direction = Sort.Direction.DESC;
        String search = "search";

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(recordRepository.findRecordByUserIdAndFilter(any(), any(), any())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> operationService.getRecordsByUserWithFilter(username, 1, 1, sortBy, direction, search));
    }

    @Test
    void softDeleteRecordTest() {
        Long recordId = 1L;

        when(recordRepository.findById(any())).thenReturn(java.util.Optional.of(new Record()));

        operationService.softDeleteRecord(recordId);
    }

    @Test
    void softDeleteRecordFailedTest() {
        Long recordId = 1L;

        when(recordRepository.findById(any())).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> operationService.softDeleteRecord(recordId));
    }
}