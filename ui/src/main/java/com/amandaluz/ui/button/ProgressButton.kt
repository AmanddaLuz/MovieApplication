package com.amandaluz.ui.button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.amandaluz.ui.R
import com.amandaluz.ui.databinding.BlueRoundedButtonBinding


class ProgressButton @JvmOverloads constructor (
    context: Context,
    attributeSet: AttributeSet?,
    defStyleAttrSet: Int = 0
): ConstraintLayout(context, attributeSet, defStyleAttrSet){

    private var mTitle: String? = null
    private var mLoadingTitle: String? = null
    private var mDisabledTitle: String? = null

    private val binding =
           BlueRoundedButtonBinding.inflate(LayoutInflater.from(context), this, true)

    private var state: ProgressButtonState = ProgressButtonState.Enabled
        set(value) {
            field = value
            refreshState()
        }

    init {
        refreshState()
    }

    fun setLayout(title: String, loadingTitle: String, disabledTitle: String) {
        mTitle = title
        mLoadingTitle = loadingTitle
        mDisabledTitle = disabledTitle
    }

    fun setLayout(title: String) {
        mTitle = title
        mLoadingTitle = title
        mDisabledTitle = title
    }

    private fun refreshState() {
        isEnabled = state.isEnabled
        isClickable = state.isEnabled
        refreshDrawableState()
        binding.container.isEnabled = state.isEnabled

        binding.txtTitle.run {
            isEnabled = state.isEnabled
            isClickable = state.isEnabled
        }

        binding.customProgress.visibility = state.progressVisibility
        when (state) {
            ProgressButtonState.Disabled -> binding.txtTitle.text = mDisabledTitle
            ProgressButtonState.Enabled -> binding.txtTitle.text = mTitle
            ProgressButtonState.Loading -> binding.txtTitle.text = mLoadingTitle
            ProgressButtonState.White -> setWhiteButton()
        }
    }

    private fun setWhiteButton() {
        binding.txtTitle.setTextColor(ResourcesCompat.getColor(resources, R.color.blue_button, null))
        binding.txtTitle.text = mTitle
        binding.container.setBackgroundResource(R.drawable.white_shape)
    }

    fun setProgressButtonClickListener(listener: ()-> Unit) {
        binding.container.setOnClickListener {
            listener.invoke()
        }
        binding.txtTitle.setOnClickListener {
            listener.invoke()
        }
    }

    fun setLoading(){
        state = ProgressButtonState.Loading
    }

    fun setNormal(){
        state = ProgressButtonState.Enabled
    }

    fun setWhite(){
        state = ProgressButtonState.White
    }

    fun setDisabled(){
        state = ProgressButtonState.Disabled
    }

    sealed class ProgressButtonState(val isEnabled: Boolean, val progressVisibility: Int){
        object Disabled: ProgressButtonState(false, View.GONE)
        object Enabled: ProgressButtonState(true, View.GONE)
        object Loading: ProgressButtonState(false, View.VISIBLE)
        object White: ProgressButtonState(true, View.GONE)
    }

    fun getText() = binding.txtTitle.text.toString()
}