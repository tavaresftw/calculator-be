package com.ntd.calculator.service;

import com.ntd.calculator.Client.RandomStringClient;
import com.ntd.calculator.model.Operation;
import com.ntd.calculator.model.User;
import com.ntd.calculator.model.enums.OperationType;
import com.ntd.calculator.repository.OperationRepository;
import com.ntd.calculator.model.Record;
import com.ntd.calculator.repository.RecordRepository;
import com.ntd.calculator.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

public class OperationService {
    private final OperationRepository operationRepository;
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final RandomStringClient randomStringClient;

    public OperationService(OperationRepository operationRepository, RecordRepository recordRepository, UserRepository userRepository, RandomStringClient randomStringClient) {
        this.operationRepository = operationRepository;
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
        this.randomStringClient = randomStringClient;
    }

    @Transactional
    public String executeOperation(String username, BigDecimal a, BigDecimal b, OperationType operationType) {
        User user = userRepository.findByUsername(username);
        Operation operation = operationRepository.findByType(operationType.name());
        checkBalance(user, operation);
        String result = switch (operationType) {
            case ADDITION -> additionOperation(a, b);
            case SUBTRACTION -> subtractionOperation(a, b);
            case MULTIPLICATION -> multiplicationOperation(a, b);
            case DIVISION -> divisionOperation(a, b);
            case SQUARE_ROOT -> squareRootOperation(a);
            case RANDOM_STRING -> randomStringOperation(a);
        };

        Record record = getRecord(user, operation, result);
        recordRepository.save(record);

        user.setBalance(user.getBalance().subtract(operation.getCost()));
        userRepository.save(user);
        return result;
    }

    private void checkBalance(User user, Operation operation) {
        if (user.getBalance().compareTo(operation.getCost()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
    }

    private Record getRecord(User user, Operation operation, String result) {
        return new Record(
                operation,
                user,
                user.getBalance(),
                user.getBalance().subtract(operation.getCost()),
                result,
                LocalDateTime.now()
        );
    }

    private String additionOperation(BigDecimal a, BigDecimal b) {
        return a.add(b).toString();
    }

    private String subtractionOperation(BigDecimal a, BigDecimal b) {
        return a.subtract(b).toString();
    }

    private String multiplicationOperation(BigDecimal a, BigDecimal b) {
        return a.multiply(b).setScale(2, RoundingMode.HALF_UP).toString();
    }

    private String divisionOperation(BigDecimal a, BigDecimal b) {
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return a.divide(b, 2, RoundingMode.HALF_UP).toString();
    }

    private String squareRootOperation(BigDecimal a) {
        if (a.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cannot calculate square root of a negative number");
        }
        return BigDecimal.valueOf(Math.sqrt(a.longValue())).setScale(2, RoundingMode.HALF_UP).toString();
    }

    private String randomStringOperation(BigDecimal a) {
        try {
            return randomStringClient.getRandomString(a.intValue()).trim();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Record> getRecordsByUser(String username) {
        User user = userRepository.findByUsername(username);
        try {
            return recordRepository.findByUser(user);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
