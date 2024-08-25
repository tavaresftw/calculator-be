package com.ntd.calculator.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SquareRootOperation implements OperationStrategy{

    @Override
    public String execute(BigDecimal a, BigDecimal b) {
        if (a.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cannot calculate square root of a negative number");
        }
        return BigDecimal.valueOf(Math.sqrt(a.longValue())).setScale(2, RoundingMode.HALF_UP).toString();
    }
}
