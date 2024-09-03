package com.ntd.calculator.repository;

import com.ntd.calculator.model.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecordRepository extends JpaRepository<Record, Long> {
    @Query("SELECT r FROM Record r WHERE r.user.id = :userId AND r.deleted = false AND " +
            "(r.operationResponse LIKE %:search% OR " +
            "r.operation.name LIKE %:search% OR " +
            "r.amount = :searchNumber OR " +
            "r.userBalance = :searchNumber OR " +
            "r.date = :searchDate)")
    Page<Record> findRecordByUserIdAndFilter(Long userId, String search, Pageable pageable);
}
