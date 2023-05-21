package com.amandaluz.ui.listener

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import com.amandaluz.ui.R
import com.amandaluz.ui.button.ProgressButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomerHistoryListenerImpl(private val context : Context) : CustomerHistoryListener {

    private var userTypedEnoughText = false
    private var reasonSelected = false
    private lateinit var buttonContinue: ProgressButton

    override fun setReasonSelected() {
        reasonSelected = true
        buttonContinue.isEnabled = userTypedEnoughText
    }

    override fun unsetReasonSelected() {
        reasonSelected = false
        buttonContinue.isEnabled = false
    }

    override fun setupHistory(editText : TextInputEditText , btnContinue : ProgressButton) {
        editText.onFocusChangeListener = View.OnFocusChangeListener { v , hasFocus ->
            updateHint(v as TextInputEditText , v.parent.parent as TextInputLayout , hasFocus)
        }
        buttonContinue = btnContinue
        setupButtonEnabling(editText)
    }

    override fun updateHint(
        editText : TextInputEditText ,
        inputLayout : TextInputLayout ,
        hasFocus : Boolean
    ) {
        if (hasFocus) {
            inputLayout.hint = ""
            inputLayout.boxStrokeColor = ContextCompat.getColor(context , R.color.black)
        } else {
            if (editText.text.isNullOrEmpty()) {
                inputLayout.hint = editText.hint
                inputLayout.boxStrokeColor = ContextCompat.getColor(context , R.color.line_gray)
            }
        }
    }

    override fun setupButtonEnabling(editText : TextInputEditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s : CharSequence? ,
                start : Int ,
                count : Int ,
                after : Int
            ) {}

            override fun onTextChanged(
                s : CharSequence? ,
                start : Int ,
                before : Int ,
                count : Int
            ) {
                userTypedEnoughText = (s?.length ?: 0) >= MIN_TEXT_INPUT_ENABLER
                buttonContinue.isEnabled = userTypedEnoughText && reasonSelected
                if (buttonContinue.isEnabled) {
                    buttonContinue.setNormal()
                }
            }

            override fun afterTextChanged(s : Editable?) {}
        })
    }

    companion object {
        private const val  MIN_TEXT_INPUT_ENABLER = 3
    }
}
