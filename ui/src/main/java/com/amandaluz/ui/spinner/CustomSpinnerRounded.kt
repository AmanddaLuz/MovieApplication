package com.amandaluz.ui.spinner

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatSpinner
import androidx.constraintlayout.widget.ConstraintLayout
import com.amandaluz.ui.R
import com.amandaluz.ui.databinding.CustomSpinnerRoundedBinding

class CustomSpinnerRounded @JvmOverloads constructor (
    context: Context,
    attributeSet: AttributeSet?,
    defStyleAttrSet: Int = 0
): ConstraintLayout(context, attributeSet, defStyleAttrSet) {

    private val binding = CustomSpinnerRoundedBinding.inflate(LayoutInflater.from(context), this, true)

    fun setList(labels: List<String>, hint: Int) {
        var newList = listOf(context.getString(hint))
        newList += labels
        val spinnerAdapter = RoundedSpinnerAdapter(
            context,
            R.layout.rounded_spinner_item,
            newList
        )

        binding.spinnerRounded.adapter = spinnerAdapter
    }

    fun emptyList() {
        binding.spinnerRounded.adapter = null
    }

    fun getSpinnerView(): AppCompatSpinner =
        binding.spinnerRounded

}