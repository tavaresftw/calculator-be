package com.ntd.calculator.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MultiplicationOperation implements OperationStrategy{
    @Override
    public String execute(BigDecimal a, BigDecimal b) {
        return a.multiply(b).setScale(2, RoundingMode.HALF_UP).toString();
    }
}
