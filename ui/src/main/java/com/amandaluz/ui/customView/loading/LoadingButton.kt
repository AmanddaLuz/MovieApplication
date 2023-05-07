package com.amandaluz.ui.customView.loading

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.amandaluz.ui.R
import com.amandaluz.ui.databinding.LoadingButtonBinding

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet?,
    defStyAttributeSet: Int = 0
) : FrameLayout(context, attributeSet, defStyAttributeSet) {

    private val binding = LoadingButtonBinding
        .inflate(LayoutInflater.from(context), this, true)
    private var label: String? = null

    init {
        setLayout(attributeSet)
    }

    private fun setLayout(attributeSet: AttributeSet?){
        attributeSet?.let { attrs ->
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton)
            val label = attributes.getString(R.styleable.LoadingButton_text).toString()
            val backgroundColor = attributes.getColor(
                R.styleable.LoadingButton_customBackgroundColor,
                ContextCompat.getColor(context, R.color.red)
            )

            binding.run {
                loadingButton.run {
                    text = label
                    isEnabled = false
                    setBackgroundColor(backgroundColor)
                }
                progressBar.run {
                    visibility = View.GONE
                }
            }

            attributes.recycle()
        }
    }

    fun progress(enabled: Boolean){
        binding.run {
            progressBar.visibility = if (enabled) View.VISIBLE else View.GONE
            loadingButton.isEnabled = enabled
            loadingButton.text = if (enabled) "" else label
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        binding.loadingButton.setOnClickListener(l)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.loadingButton.isEnabled = enabled
    }
}