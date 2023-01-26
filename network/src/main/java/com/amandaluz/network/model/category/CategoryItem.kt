package com.amandaluz.network.model.category

import com.amandaluz.network.model.movie.Result

data class CategoryItem(
    val title: String,
    val result: List<Result>
)
