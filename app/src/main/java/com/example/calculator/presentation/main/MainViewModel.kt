package com.example.calculator.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.data.calculateExpression
import com.example.calculator.domain.HistoryRepository
import com.example.calculator.domain.SettingsDao
import com.example.calculator.domain.entity.HistoryItem
import com.example.calculator.domain.entity.ResultPanelType
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingsDao: SettingsDao,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private var expression: String = ""

    private val _expressionState = MutableLiveData<ExpressionState>(ExpressionState(expression,0))
    val expressionState: LiveData<ExpressionState> = _expressionState

    private val _resultState = MutableLiveData<String>()
    val resultState: LiveData<String> = _resultState

    private val _resultPanelState = MutableLiveData<ResultPanelType>()
    val resultPanelState: LiveData<ResultPanelType> = _resultPanelState

    init {
        viewModelScope.launch {
            _resultState.value = settingsDao.getResultPanelType().toString()
        }
    }

    fun onNumberClick(number: Int, selection: Int) {
        expression = putInSelection(expression, number.toString(), selection)
        _expressionState.value = ExpressionState(expression, selection + 1)
    }

    fun onOperatorClick(operator: Operator, selection: Int) {
        expression = putInSelection(expression, operator.symbol, selection)
        _expressionState.value = ExpressionState(expression, selection + 1)
//        _resultState.value = calculateExpression(expression)
    }

    fun onEqualsClick(equals: Operator, text: CharSequence) {
        val result = calculateExpression(expression)
        viewModelScope.launch {
            historyRepository.add(HistoryItem(expression, result))
        }
        _resultState.value = result
        _expressionState.value = ExpressionState(result, result.length)
        expression = result
    }

    fun onBackSpaceClick(selection: Int) {
        expression = expression.removeRange(selection -1, selection)
        _expressionState.value = ExpressionState(expression, (selection - 1).coerceAtLeast(0))
//        _resultState.value = calculateExpression(expression)
    }

    fun onClearClick() {
        expression = ""
        _expressionState.value = ExpressionState("", 0)
        _resultState.value = expression
    }



    override fun onCleared() {
        super.onCleared()
        Log.d("MainViewModel", "onCleared")
    }

    private fun putInSelection(expression: String, put: String, selection: Int): String{
        return expression.substring(0, selection) + put + expression.substring(selection, expression.length)
    }

    fun onStart() {
        viewModelScope.launch {
            _resultState.value = settingsDao.getResultPanelType().toString()
        }
    }

    fun onHistoryResult(item: HistoryItem?) {
        if (item != null) {
            expression = item.expression
            _expressionState.value = ExpressionState(expression, expression.length)
            _resultState.value = item.result
        }

    }

}
enum class Operator(val symbol: String) {
    MINUS("-"), PLUS("+"), MULTIPUE("*"),DEVIDE("/"), DOT ("."), EQUALS("=")
}
class ExpressionState(val expression: String, val selection: Int)
