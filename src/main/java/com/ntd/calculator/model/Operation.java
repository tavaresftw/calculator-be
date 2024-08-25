package com.ntd.calculator.model;

import com.ntd.calculator.model.enums.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "operations")
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private OperationType type;
    private BigDecimal cost;

}
