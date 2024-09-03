package com.ntd.calculator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "records")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "OPERATION_ID", nullable = false)
    private Operation operation;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "USER_BALANCE")
    private BigDecimal userBalance;

    @Column(name = "OPERATION_RESPONSE")
    private String operationResponse;

    @Column(name = "DATE")
    private LocalDateTime date;

    @Column(name = "DELETED")
    private boolean deleted = false;

    public Record(Operation operation, User user, BigDecimal amount, BigDecimal userBalance, String operationResponse, LocalDateTime date) {
        this.operation = operation;
        this.user = user;
        this.amount = amount;
        this.userBalance = userBalance;
        this.operationResponse = operationResponse;
        this.date = date;
    }
}