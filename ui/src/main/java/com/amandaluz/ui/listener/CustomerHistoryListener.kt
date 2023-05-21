package com.amandaluz.ui.listener

import com.amandaluz.ui.button.ProgressButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

interface CustomerHistoryListener {
    fun setupHistory(editText : TextInputEditText, btnContinue : ProgressButton)

    fun updateHint(
        editText : TextInputEditText ,
        inputLayout : TextInputLayout ,
        hasFocus : Boolean
    )

    fun setupButtonEnabling(editText : TextInputEditText)

    fun setReasonSelected()

    fun unsetReasonSelected()
}