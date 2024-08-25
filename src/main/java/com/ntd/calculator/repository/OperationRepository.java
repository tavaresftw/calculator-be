package com.ntd.calculator.repository;

import com.ntd.calculator.model.Operation;
import com.ntd.calculator.model.enums.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    Operation findByType(OperationType type);
}
