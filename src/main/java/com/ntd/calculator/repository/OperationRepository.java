package com.ntd.calculator.repository;

import com.ntd.calculator.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    Operation findByType(String type);
}
