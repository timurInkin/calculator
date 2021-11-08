package com.example.calculator.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.domain.HistoryRepository
import com.example.calculator.domain.SettingsDao
import com.example.calculator.domain.calculateExpression
import com.example.calculator.domain.entity.HistoryItem
import com.example.calculator.domain.entity.ResultPanelType
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class MainViewModel(
    private val settingsDao: SettingsDao,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private var expression: String = ""

    private val _expressionState = MutableLiveData<ExpressionState>(ExpressionState(expression,0))
    val expressionState: LiveData<ExpressionState> = _expressionState

    private val _resultState = MutableLiveData<String>()
    val resultState: LiveData<String> = _resultState

    private val _resultPanelState = MutableLiveData<ResultPanelType>(ResultPanelType.RIGHT)
    val resultPanelState: LiveData<ResultPanelType> = _resultPanelState

    private val _vibrationTime = MutableLiveData<Int>()
    val vibrationTime = _vibrationTime

    private val _precisionResult = MutableLiveData<Int>()
    val precisionResult = _precisionResult




    init {
        viewModelScope.launch {
            _resultPanelState.value = settingsDao.getResultPanelType()
            _precisionResult.value = settingsDao.getPrecision()
            _vibrationTime.value = settingsDao.getVibration()


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

    fun onEqualsClick() {
        try {
            val result = calculateExpression(expression, precisionResult.value ?: 0)
            _resultState.value = result
            viewModelScope.launch {
                historyRepository.add(HistoryItem(
                    expression,
                    result,
                    LocalDateTime.now()
                ))
            }
        } catch (e: java.lang.IllegalArgumentException) {
            // do nothing
        }
    }
    fun onBackSpaceClick(selection: Int) {
        if (selection <= 0) {
            return
        }
        expression = expression.removeRange(selection -1, selection)
        _expressionState.value = ExpressionState(expression, (selection - 1).coerceAtLeast(0))
    }


    fun onClearClick() {
        expression = ""
        _expressionState.value = ExpressionState("", 0)
        _resultState.value = ""
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
//            _resultState.value = settingsDao.getResultPanelType().toString()
            _resultPanelState.value = settingsDao.getResultPanelType()
            _precisionResult.value = settingsDao.getPrecision()
            _vibrationTime.value = settingsDao.getVibration()
            Log.d("mainViewModel","%d %d".format(_precisionResult.value,_vibrationTime.value))

        }
    }

    fun onHistoryResult(item: HistoryItem?) {
        if (item != null) {
            expression = item.expression
            _expressionState.value = ExpressionState(expression, expression.length)
            _resultState.value = item.result
        }

    }
    fun onSqrtClick(selection: Int) {
        onOperatorClick(Operator.DEGREE, selection)
        onOperatorClick(Operator.LEFT_BRACE, selection + 1)
        onNumberClick(0, selection + 2)
        onOperatorClick(Operator.DOT, selection + 3)
        onNumberClick(5, selection + 4)
        onOperatorClick(Operator.RIGHT_BRACE, selection + 5)
    }
}

