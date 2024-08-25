package com.ntd.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableConfigurationProperties
@PropertySource({"classpath:application.properties"})
@ComponentScan(basePackages = "com.ntd.calculator")
public class CalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalculatorApplication.class, args);
	}

}
