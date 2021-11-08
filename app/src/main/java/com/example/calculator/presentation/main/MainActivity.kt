package com.example.calculator.presentation.main

import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import android.view.Gravity
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
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
import com.example.calculator.presentation.history.HistoryResult

class MainActivity : BaseActivity() {

    private val viewBinding by viewBinding(MainActivityBinding::bind)
    private val viewModel: MainViewModel by viewModels() {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainViewModel(
                    SettingsDaoProvider.get(this@MainActivity),
                    HistoryRepositoryProvider.get(this@MainActivity)
                ) as T
            }
        }
    }

    private val resultLauncher = registerForActivityResult(HistoryResult()) { item ->
        viewModel.onHistoryResult(item)
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)




        viewBinding.mainInput.apply {
            showSoftInputOnFocus = false
        }

        viewBinding.mainActivitySettings.setOnClickListener {
            openSettings()
            vibrate()
        }

        viewBinding.mainHistory.setOnClickListener {
            openHistory()
            vibrate()
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
            textView.setOnClickListener {
                viewModel.onNumberClick(
                    index,
                    viewBinding.mainInput.selectionStart
                )
                vibrate()
            }
        }

        mapOf(
            Operator.PLUS to viewBinding.mainPlus,
            Operator.MINUS to viewBinding.mainMinus,
            Operator.MULTIPLY to viewBinding.mainMultiply,
            Operator.DIVIDE to viewBinding.mainDev,
            Operator.DOT to viewBinding.mainDot,
            Operator.DEGREE to viewBinding.mainDegree,
        ).forEach { (operator, textView) ->
            textView?.setOnClickListener {
                viewModel.onOperatorClick(operator, viewBinding.mainInput.selectionStart)
                vibrate()
            }
        }


        viewBinding.mainSqrt?.setOnClickListener {
            viewModel.onSqrtClick(viewBinding.mainInput.selectionStart)
            vibrate()
        }

        viewBinding.mainEquals.setOnClickListener {
            viewModel.onEqualsClick()
            vibrate()
        }

        viewBinding.mainBackspace.setOnClickListener {
            viewModel.onBackSpaceClick(viewBinding.mainInput.selectionStart)
        vibrate()
        }


        viewBinding.mainClear.setOnClickListener {viewModel.onClearClick()
       vibrate()
        }

        viewModel.resultPanelState.observe(this) {
            with(viewBinding.mainResult) {
                gravity = when (it) {
                    ResultPanelType.LEFT -> Gravity.START.or(Gravity.CENTER_VERTICAL)
                    ResultPanelType.RIGHT -> Gravity.END or (Gravity.CENTER_VERTICAL)
                    ResultPanelType.HIDE -> gravity
                }
                isVisible = it != ResultPanelType.HIDE
            }
        }

        viewModel.expressionState.observe(this) {
            viewBinding.mainInput.setText(it.expression)
            viewBinding.mainInput.setSelection(it.selection)
        }
        viewModel.resultState.observe(this) {
            viewBinding.mainResult.text = it.toString()

        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.onStart()


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

    private fun vibrate() {
        if (viewModel.vibrationTime.value ?:0 > 0) {
            val vibe = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibe.vibrate(
                VibrationEffect.createOneShot(
                    (viewModel.vibrationTime.value ?: 1).toLong(), 1
                )
            )
        }
    }


}
