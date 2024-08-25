package com.ntd.calculator.data;

import com.ntd.calculator.model.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OperationRequest {
    private OperationType operationType;
    private BigDecimal num1 = null;
    private BigDecimal num2 = null;
}
