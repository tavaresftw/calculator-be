package com.ntd.calculator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "records")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "operation_id", nullable = false)
    private Operation operation;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private BigDecimal amount;
    private BigDecimal userBalance;
    private String operationResponse;
    private LocalDateTime date;

    public Record(Operation operation, User user, BigDecimal amount, BigDecimal userBalance, String operationResponse, LocalDateTime date) {
        this.operation = operation;
        this.user = user;
        this.amount = amount;
        this.userBalance = userBalance;
        this.operationResponse = operationResponse;
        this.date = date;
    }
}
