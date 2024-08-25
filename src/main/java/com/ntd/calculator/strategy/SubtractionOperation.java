package com.ntd.calculator.strategy;

import java.math.BigDecimal;

public class SubtractionOperation implements OperationStrategy {
    @Override
    public String execute(BigDecimal a, BigDecimal b) {
        return a.subtract(b).toString();
    }
}