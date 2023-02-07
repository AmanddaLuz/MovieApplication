package com.amandaluz.ui.customView.flipper.model

import com.amandaluz.ui.R

data class Poster(
    var image: Int
)

fun posterList(): MutableList<Poster> {
    return mutableListOf(
        Poster(R.drawable.ic_launcher_foreground),
        Poster(R.drawable.ic_launcher_background),
        Poster(R.drawable.ic_launcher_foreground)
    )
}