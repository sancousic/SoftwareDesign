package com.example.calculator;

import org.mariuszgromada.math.mxparser.Expression;

public class Calculator {
    static double exec(String expr) {
        Expression expression = new Expression(expr);
        return expression.calculate();
    }
}
