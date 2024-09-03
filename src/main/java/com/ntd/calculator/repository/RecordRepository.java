package com.ntd.calculator.repository;

import com.ntd.calculator.model.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecordRepository extends JpaRepository<Record, Long> {
    @Query("SELECT r FROM Record r WHERE r.user.id = :userId AND r.deleted = false " +
            "AND (:search IS NULL OR :search = '' OR (" +
            "LOWER(r.operationResponse) LIKE LOWER(:search) OR " +
            "LOWER(r.operation.type) LIKE LOWER(:search) OR " +
            "CAST(r.amount AS string) LIKE LOWER(:search) OR " +
            "CAST(r.userBalance AS string) LIKE LOWER(:search) OR " +
            "CAST(r.date AS string) LIKE LOWER(:search)))")
    Page<Record> findRecordByUserIdAndFilter(Long userId, String search, Pageable pageable);
}
