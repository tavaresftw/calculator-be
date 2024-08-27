package com.ntd.calculator.strategy;

import com.ntd.calculator.client.RandomStringClient;

import java.math.BigDecimal;

public class RandomStringOperation implements OperationStrategy {

    private final RandomStringClient randomStringClient;

    public RandomStringOperation(RandomStringClient randomStringClient) {
        this.randomStringClient = randomStringClient;
    }


    @Override
    public String execute(BigDecimal a, BigDecimal b) {
        try {
            return randomStringClient.getRandomString(a.intValue()).trim();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
