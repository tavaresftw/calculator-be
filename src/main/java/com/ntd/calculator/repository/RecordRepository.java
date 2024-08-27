package com.ntd.calculator.repository;

import com.ntd.calculator.model.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {
    Page<Record> findRecordByUserIdOrderByIdDesc(Long userId, Pageable pageable);
}
