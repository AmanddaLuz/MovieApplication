package com.amandaluz.ui.customView.dots

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.amandaluz.ui.R
import com.amandaluz.ui.databinding.ItemCustomviewDotsBinding

class CustomDots(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    companion object{
        const val DEFAUT_SIZE_DOT = 48F
        const val DEFAUT_COLOR_SELECTED_DOT = android.R.color.white
        val DEFAUT_COLOR_NOT_SELECTED_DOT = R.color.arrow
    }

    private var dotSize = DEFAUT_SIZE_DOT
    private var colorDotSelected: Int = ContextCompat.getColor(context, DEFAUT_COLOR_SELECTED_DOT)
    private var colorDotNotSelected = ContextCompat.getColor(context, DEFAUT_COLOR_NOT_SELECTED_DOT)

    init {
        setLayout(attrs)
    }

    private fun setLayout(attrs: AttributeSet?) {
        attrs?.let { attributeSet ->
            val at = context.obtainStyledAttributes(
                attributeSet,
                R.styleable.CustomDots
            )

            val dotColorItemSelected = at.getResourceId(R.styleable.CustomDots_dotColorSelected, 0)
            if(dotColorItemSelected != 0){
                colorDotSelected = ContextCompat.getColor(context, dotColorItemSelected)
            }

            val colorNotSelected = at.getResourceId(R.styleable.CustomDots_dotColorUnSelected, 0)
            if(colorNotSelected != 0){
                colorDotNotSelected =  ContextCompat.getColor(context, colorNotSelected)
            }

            val mDotSizeId = at.getResourceId(R.styleable.CustomDots_dotSize, 0)
            if (mDotSizeId != 0) {
                dotSize = resources.getDimensionPixelSize(mDotSizeId).toFloat()
            }
            at.recycle()
        }
    }

    fun setLayout(
        size: Int, position: Int = 0
    ) {

        val binding = ItemCustomviewDotsBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

        removeAllViews()
        Array(size) {
            val textView = TextView(context).apply {
                text = context.getText(R.string.dot)
                textSize = dotSize
                this.setPadding(0,0,20, 0)
                setTextColor(
                    if (position == it) colorDotSelected
                    else colorDotNotSelected
                )
            }
            addView(textView)
        }
        this.addView(binding.root)
    }
}