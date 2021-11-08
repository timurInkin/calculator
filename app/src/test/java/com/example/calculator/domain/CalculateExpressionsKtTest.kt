package com.example.calculator.domain

import org.junit.Assert
import org.junit.Test

class CalculateExpressionsKtTest {

    @Test
    fun testPlus() {
        val expression = "2+2"
        val result = "4"
        Assert.assertEquals(result, calculateExpression(expression))
    }
    @Test
    fun testSubtraction(){
        val expression = "4-2"
        val result = "2"
        Assert.assertEquals(result, calculateExpression(expression))
    }
    @Test
    fun testExpression(){
        val expression = "4-2*2"
        val result = "0"
        Assert.assertEquals(result, calculateExpression(expression))
    }
    @Test
    fun testI() {
        testCalculation("", "")
        testCalculation("2+", "2")
        testCalculation("2-", "2")
        testCalculation("2+2", "4")
        testCalculation("", "")
    }
    private fun testCalculation(
        expression: String,
        result: String
    ){
        Assert.assertEquals(result, calculateExpression(expression))
    }

}