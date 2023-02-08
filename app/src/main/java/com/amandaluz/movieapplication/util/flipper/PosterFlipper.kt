package com.amandaluz.movieapplication.util.flipper

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ViewFlipper
import com.amandaluz.core.util.url.initPath
import com.amandaluz.core.util.url.linkPathNull
import com.amandaluz.network.model.domain.PosterDomain
import com.amandaluz.ui.databinding.ItemPosterBinding
import com.bumptech.glide.Glide

class PosterFlipper(context: Context, attrs: AttributeSet?) : ViewFlipper(context, attrs) {

    private var list = mutableListOf<PosterDomain>()

    fun setList(posterList: MutableList<PosterDomain>) {
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
                titlePoster.text = it.title

                val initPath = initPath()
                val pathNull = linkPathNull()

                Glide.with(context)
                    .load(if (it.poster_path.isNullOrEmpty()) pathNull else initPath.plus(it.poster_path))
                    .centerCrop()
                    .into(ivPoster)

                addView(root)
            }
        }
    }
}