package com.ntd.calculator.repository;

import com.ntd.calculator.model.Record;
import com.ntd.calculator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findRecordByUserIdOrderByIdDesc(Long userId);
}
