package com.ntd.calculator.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class RecordsResponse {
    private Long id;
    private String operationType;
    private BigDecimal amount;
    private String operationResponse;
    private String date;
    private BigDecimal userBalance;
}
