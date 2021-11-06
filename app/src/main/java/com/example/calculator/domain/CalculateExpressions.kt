package com.example.calculator.data

import com.fathzer.soft.javaluator.DoubleEvaluator
import kotlin.math.floor

fun calculateExpression(expression: String) : String {

    if (expression.isBlank()) return ""

    var formattedExpression = expression
    while (!formattedExpression.last().isDigit()) {
        formattedExpression = formattedExpression.dropLast(1)
    }

    val result = DoubleEvaluator().evaluate(formattedExpression)


    return if (floor(result) == result) {
        result.toInt().toString()
    } else {
        result.toString()
    }
}
