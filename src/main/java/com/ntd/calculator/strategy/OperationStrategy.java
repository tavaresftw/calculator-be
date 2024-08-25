package com.ntd.calculator.strategy;

import java.math.BigDecimal;

public interface OperationStrategy {
    String execute(BigDecimal a, BigDecimal b);
}
