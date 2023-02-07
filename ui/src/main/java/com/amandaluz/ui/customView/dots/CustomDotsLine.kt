package com.amandaluz.ui.customView.dots

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.amandaluz.ui.R
import com.amandaluz.ui.databinding.ItemCustomDotsLineBinding

class CustomDotsLine(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    companion object {
        const val DEFAUT_SIZE_DOT = 22F
        val DEFAUT_SELECTED_DOT = R.drawable.ic_dot_full_blue
        val DEFAUT_NOT_SELECTED_DOT = R.drawable.ic_dot_empty
        val DEFAULT_LINE_DOT_BLUE = R.drawable.ic_line_dot_full_blue
        val DEFAULT_LINE_DOT_WHITE = R.drawable.ic_line_dot_empty
    }

    private var dotSize = DEFAUT_SIZE_DOT
    private var dotSelected = ContextCompat.getDrawable(context, DEFAUT_SELECTED_DOT)
    private var dotNotSelected = ContextCompat.getDrawable(context, DEFAUT_NOT_SELECTED_DOT)
    private var lineDotBlue = ContextCompat.getDrawable(context, DEFAULT_LINE_DOT_BLUE)
    private var lineDotWhite = ContextCompat.getDrawable(context, DEFAULT_LINE_DOT_WHITE)

    init {
        setLayout(attrs)
    }

    private fun setLayout(attrs: AttributeSet?) {
        attrs?.let { attributeSet ->
            val at = context.obtainStyledAttributes(
                attributeSet,
                R.styleable.CustomDotsLine
            )

            val dotColorItemSelected = at.getResourceId(R.styleable.CustomDotsLine_dotSelectedLine, 0)
            if (dotColorItemSelected != 0) {
                dotSelected = ContextCompat.getDrawable(context, dotColorItemSelected)
            }

            val colorNotSelected = at.getResourceId(R.styleable.CustomDotsLine_dotUnSelectedLine, 0)
            if (colorNotSelected != 0) {
                dotNotSelected = ContextCompat.getDrawable(context, colorNotSelected)
            }

            val mDotSizeId = at.getResourceId(R.styleable.CustomDotsLine_dotSizeLine, 0)
            if (mDotSizeId != 0) {
                dotSize = resources.getDimensionPixelSize(mDotSizeId).toFloat()
            }

            val mDotLineBlueId = at.getResourceId(R.styleable.CustomDotsLine_dotLineBlueLine, 0)
            if (mDotSizeId != 0) {
                lineDotBlue = ContextCompat.getDrawable(context, mDotLineBlueId)
            }
            val mDotLineWhiteId = at.getResourceId(R.styleable.CustomDotsLine_dotLineWhiteLine, 0)
            if (mDotSizeId != 0) {
                lineDotWhite = ContextCompat.getDrawable(context, mDotLineWhiteId)
            }
            at.recycle()
        }
    }

    fun setLayout(
        size: Int, position: Int = 0
    ) {

        val binding = ItemCustomDotsLineBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

        removeAllViews()
        Array(size) {
            val imageDotFull = ImageView(context).apply {
                setImageDrawable(
                    setDrawable(position, it)
                )
            }
            addView(imageDotFull)
        }
        this.addView(binding.root)
    }

    private fun setDrawable(position: Int, it: Int) = when (position) {
        it -> drawableBlue(it)
        else -> drawableWhite(it)
    }

    private fun drawableWhite(it: Int) = when (it) {
        0 -> dotNotSelected
        else -> lineDotWhite
    }

    private fun drawableBlue(it: Int) = when (it) {
        0 -> dotSelected
        else -> lineDotBlue
    }
}