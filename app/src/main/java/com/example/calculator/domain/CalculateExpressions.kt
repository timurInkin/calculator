package com.example.calculator.domain

import com.fathzer.soft.javaluator.DoubleEvaluator
import kotlin.math.floor

fun calculateExpression(expression: String, precision: Int): String {


    if (expression.isBlank()) return ""

    val result = DoubleEvaluator().evaluate(expression)

    return if (floor(result) == result) {
        result.toInt().toString()
    } else {
        "%.${precision}f".format(result)
    }
}

