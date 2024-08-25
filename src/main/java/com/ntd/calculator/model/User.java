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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;

    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD",nullable = false)
    private String password;

    @Column(name = "STATUS",nullable = false)
    private String status = null;

    @Column(name = "BALANCE",nullable = false)
    private BigDecimal balance = null;

    @Column(name = "CREATED_AT",updatable = false)
    private LocalDateTime createdAt = null;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt = null;

    public User(String username, String password, String status, BigDecimal balance, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.username = username;
        this.password = password;
        this.status = status;
        this.balance = balance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User(String username, String password, String status, BigDecimal balance) {
        this.username = username;
        this.password = password;
        this.status = status;
        this.balance = balance;
    }
}
