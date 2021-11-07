package com.example.calculator.presentation.main

import android.content.Context
import android.os.Build
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
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

//private val Context.HAPTIC_FEEDBACK_DURATION: Long
//    get() {}

class MainViewModel(
    private val settingsDao: SettingsDao,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private var expression: String = ""

    private val _expressionState = MutableLiveData<ExpressionState>(ExpressionState(expression,0))
    val expressionState: LiveData<ExpressionState> = _expressionState

    private val _resultState = MutableLiveData<String>()
    val resultState: LiveData<String> = _resultState

    private val _resultPanelState = MutableLiveData<ResultPanelType>(ResultPanelType.LEFT)
    val resultPanelState: LiveData<ResultPanelType> = _resultPanelState


//    init {
//        viewModelScope.launch {
//            _resultState.value = settingsDao.getResultPanelType().toString()
//        }
//    }


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

            try {
                historyRepository.add(HistoryItem(expression, result))
                _resultState.value = result
//                _expressionState.value = ExpressionState(result, result.length)
                expression = result
            } catch (e: java.lang.IllegalArgumentException) {
                //nope
            }
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
//        Fragment().vibratePhone()
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
    @RequiresApi(Build.VERSION_CODES.R)
    fun onSqrtClick(selection: Int) {
        onOperatorClick(Operator.DEGREE, selection)
        onNumberClick(0, selection + 1)
        onOperatorClick(Operator.DOT, selection + 2)
        onNumberClick(5, selection + 3)
        selection + 1
    }







}
enum class Operator(val symbol: String) {
    MINUS("-"), PLUS("+"), MULTIPUE("*"),DEVIDE("/"), DOT ("."), EQUALS("="), DEGREE ("^"), LEFT_BRACE ("("), RIGHT_BRACE (")")
}
class ExpressionState(val expression: String, val selection: Int)
