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
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String status = null;

    @Column(nullable = false)
    private BigDecimal balance = null;

    @Column(updatable = false)
    private LocalDateTime createdAt = null;

    @Column
    private LocalDateTime updatedAt;

    public User(String username, String password, String status, BigDecimal balance, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.username = username;
        this.password = password;
        this.status = status;
        this.balance = balance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
