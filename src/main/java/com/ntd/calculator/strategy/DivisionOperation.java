package com.ntd.calculator.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DivisionOperation implements OperationStrategy{
    @Override
    public String execute(BigDecimal a, BigDecimal b) {
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return a.divide(b, 2, RoundingMode.HALF_UP).toString();
    }
}
