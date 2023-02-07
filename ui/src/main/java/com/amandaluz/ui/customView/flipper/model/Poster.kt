package com.amandaluz.ui.customView.flipper.model

import com.amandaluz.ui.R

data class Poster(
    var image: Int,
    var title: String
)

fun posterList(): MutableList<Poster> {
    return mutableListOf(
        Poster(R.drawable.ic_launcher_foreground, "Aqui você pode encontrar seus filmes preferidos!"),
        Poster(R.drawable.ic_launcher_background, "E também os filmes que estão para serem lançados!"),
        Poster(R.drawable.ic_launcher_foreground, "Classifique os filmes nos enviando sua nota de 0 a 10!")
    )
}