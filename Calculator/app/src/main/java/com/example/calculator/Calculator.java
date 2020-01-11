package com.example.calculator;

import org.mariuszgromada.math.mxparser.Expression;

public class Calculator {
    static String exec(String expr) {
        Expression expression = new Expression(expr);
        expression.checkLexSyntax();
        String err = expression.getErrorMessage();
        double res =  expression.calculate();
        if(err != "Syntax status unknown.") return "Invalid syntax";
        if(Double.isNaN(res)) return "Invalid expression";;
        return Double.toString(res);
    }
}
