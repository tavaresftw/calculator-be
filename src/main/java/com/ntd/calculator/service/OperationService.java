package com.ntd.calculator.service;

import com.ntd.calculator.Client.RandomStringClient;
import com.ntd.calculator.data.RecordsResponse;
import com.ntd.calculator.model.Operation;
import com.ntd.calculator.model.User;
import com.ntd.calculator.model.enums.OperationType;
import com.ntd.calculator.repository.OperationRepository;
import com.ntd.calculator.model.Record;
import com.ntd.calculator.repository.RecordRepository;
import com.ntd.calculator.repository.UserRepository;
import com.ntd.calculator.strategy.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OperationService {
    private final OperationRepository operationRepository;
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final RandomStringClient randomStringClient;
    private final Map<OperationType, OperationStrategy> operationStrategies;

    public OperationService(OperationRepository operationRepository,
                            RecordRepository recordRepository,
                            UserRepository userRepository,
                            RandomStringClient randomStringClient
    ) {
        this.operationRepository = operationRepository;
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
        this.randomStringClient = randomStringClient;

        this.operationStrategies = Map.of(
                OperationType.ADDITION, new AdditionOperation(),
                OperationType.SUBTRACTION, new SubtractionOperation(),
                OperationType.MULTIPLICATION, new MultiplicationOperation(),
                OperationType.DIVISION, new DivisionOperation(),
                OperationType.SQUARE_ROOT, new SquareRootOperation(),
                OperationType.RANDOM_STRING, new RandomStringOperation(randomStringClient)
        );
    }

    @Transactional
    public String executeOperation(String username, BigDecimal a, BigDecimal b, OperationType operationType) {
        User user = userRepository.findByUsername(username);
        Operation operation = operationRepository.findByType(operationType);
        checkBalance(user, operation);

        OperationStrategy strategy = operationStrategies.get(operationType);
        String result = strategy.execute(a, b);

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

    public List<RecordsResponse> getRecordsByUser(String username) {
        User user = userRepository.findByUsername(username);
        try {
            List<Record> record = recordRepository.findRecordByUserIdOrderByIdDesc(user.getId());
            return getRecordsResponse(record);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<RecordsResponse> getRecordsResponse(List<Record> record) {
        List<RecordsResponse> recordsResponses = new ArrayList<>();
        for (Record r : record) {
            recordsResponses.add(new RecordsResponse(
                    r.getId(),
                    r.getOperation().getType().name(),
                    r.getAmount(),
                    r.getOperationResponse(),
                    r.getDate().toString(),
                    r.getUserBalance()
            ));
        }
        return recordsResponses;
    }
}
