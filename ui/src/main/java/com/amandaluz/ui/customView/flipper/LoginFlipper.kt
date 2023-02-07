package com.amandaluz.ui.customView.flipper

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ViewFlipper
import com.amandaluz.ui.customView.flipper.model.Poster
import com.amandaluz.ui.databinding.ItemPosterBinding
import com.bumptech.glide.Glide

class LoginFlipper(context: Context, attrs: AttributeSet?) : ViewFlipper(context, attrs) {

    private var list = mutableListOf<Poster>()

    fun setList(posterList: MutableList<Poster>) {
        this.list = posterList
    }

    fun getListSize(): Int {
        return this.list.size
    }

    fun getItemData(position: Int) = list[position]

    fun setLayout() {
        list.forEach {
            val binding = ItemPosterBinding.inflate(
                LayoutInflater.from(context),
                this,
                false
            )
            binding.apply {
                Glide.with(context).load(it.image).into(ivPoster)
                addView(root)
            }
        }
    }
}