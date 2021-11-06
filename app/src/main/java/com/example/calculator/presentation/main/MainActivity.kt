package com.example.calculator.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.Vibrator.VIBRATION_EFFECT_SUPPORT_YES
import android.view.Gravity
import android.widget.Button
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.calculator.presentation.common.BaseActivity
import com.example.calculator.R
import com.example.calculator.databinding.MainActivityBinding
import com.example.calculator.di.HistoryRepositoryProvider
import com.example.calculator.di.SettingsDaoProvider
import com.example.calculator.domain.entity.ResultPanelType
import com.example.calculator.presentation.settings.SettingsActivity
import com.example.presentation.history.HistoryResult


class MainActivity : BaseActivity() {

    private val viewBinding by viewBinding(MainActivityBinding::bind)
    private val viewModel: MainViewModel by viewModels() {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainViewModel(
                    SettingsDaoProvider.get(this@MainActivity),
                    HistoryRepositoryProvider.get(this@MainActivity)) as T
            }
        }
    }

    private val resultLauncher = registerForActivityResult(HistoryResult()) { item ->
        viewModel.onHistoryResult(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)


        viewBinding.mainInput.apply {
            showSoftInputOnFocus = false
        }

        viewBinding.mainActivitySettings.setOnClickListener {
            openSettings()
        }

        viewBinding.mainHistory.setOnClickListener {
            openHistory()
        }


        viewModel.expressionState.observe(this) {
            viewBinding.mainInput.setText(it.expression)
            viewBinding.mainInput.setSelection(it.selection)
        }
        viewModel.resultState.observe(this) {
            viewBinding.mainResult.text = it
        }

        viewModel.resultPanelState.observe(this) {
            with(viewBinding.mainResult) {
                gravity = when (it) {
                    ResultPanelType.LEFT -> Gravity.START.or(Gravity.CENTER_VERTICAL)
                    ResultPanelType.RIGHT -> Gravity.END.or(Gravity.CENTER_VERTICAL)
                    ResultPanelType.HIDE -> viewBinding.mainResult.gravity
                }
                isVisible = it != ResultPanelType.HIDE
            }

        }

        listOf(
            viewBinding.mainZero,
            viewBinding.mainOne,
            viewBinding.mainTwo,
            viewBinding.mainThree,
            viewBinding.mainFour,
            viewBinding.mainFive,
            viewBinding.mainSix,
            viewBinding.mainSeven,
            viewBinding.mainEight,
            viewBinding.mainNine,
        ).forEachIndexed { index, textView ->
            textView.setOnClickListener { viewModel.onNumberClick(index, viewBinding.mainInput.selectionStart) }
        }

        mapOf(
            Operator.PLUS to viewBinding.mainPlus,
            Operator.MINUS to viewBinding.mainMinus,
            Operator.MULTIPUE to viewBinding.mainMultiply,
            Operator.DEVIDE to viewBinding.mainDev,
            Operator.DOT to viewBinding.mainDot,
            Operator.DEGREE to viewBinding.mainDegree,
        ).forEach { (operator, textView) ->
            textView?.setOnClickListener {
                viewModel.onOperatorClick(operator, viewBinding.mainInput.selectionStart)
            }
        }


        viewBinding.mainSqrt.setOnClickListener {
            viewModel.onSqrtClick(viewBinding.mainInput.selectionStart)
        }
        viewBinding.mainEquals.setOnClickListener{viewModel.onEqualsClick(Operator.EQUALS, viewBinding.mainResult.text)}
        viewBinding.mainBackspace.setOnClickListener{viewModel.onBackSpaceClick(viewBinding.mainInput.selectionStart)}
        viewBinding.mainClear.setOnClickListener{viewModel.onClearClick()}

    }


    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    private fun openSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))

    }
    private fun openHistory() {
        resultLauncher.launch()
    }
}