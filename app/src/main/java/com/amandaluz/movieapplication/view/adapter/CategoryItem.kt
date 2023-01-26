package com.amandaluz.movieapplication.view.adapter

import com.amandaluz.network.model.movie.Result

data class CategoryItem(
    val title: String,
    val result: List<Result>
)
