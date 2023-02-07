package com.amandaluz.movieapplication.util.bottomsheet

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.amandaluz.core.util.url.linkPathNull
import com.amandaluz.hawk.ModuleHawk
import com.amandaluz.hawk.MovieKeys
import com.amandaluz.movieapplication.R
import com.amandaluz.network.model.movie.Result
import com.amandaluz.network.model.trailer.ResultTrailer
import com.amandaluz.ui.customView.bottomsheet.BottomSheetDetail

fun callBottomSheet(
    bottomSheetDetail: BottomSheetDetail,
    movie: Result,
    context: Context,
    buttonConfirm: () -> Unit,
    buttonFavorite: () -> Unit,
    imageButton: Int,
    manager: FragmentManager,
    tag: String
) {
    bottomSheetDetail.apply {
        viewTargetPoster(validatePoster(movie))
        viewTargetDetail(validatePoster(movie))
        setTitle(validateDescription(movie.title, context))
        setNota(buildString {
            append(context.getString(R.string.average))
            append(movie.vote_average)
        })
        setDescription(buildString {
            append(context.getString(R.string.votes))
            append(movie.vote_count)
        })
        setDetail(validateDescription(movie.overview, context))
        buttonCloseAction { it.dismiss() }
        buttonConfirmAction {
            buttonConfirm.invoke()
            it.dismiss()
        }
        buttonFavoriteAction {
            buttonFavorite.invoke()
        }
        setImageButton(imageButton)
        show(manager, tag)
    }
}

fun getHomeTrailerKey(
    isConnection: Boolean,
    trailerList: List<ResultTrailer>,
    trailerResponse: List<ResultTrailer>
): String? {
    return if (isConnection) {
        var key: String? = null
        trailerList.forEach {
            key = it.key
        }
        key
    } else {
        var key: String? = null
        trailerResponse.forEach {
            key = it.key
        }
        key
    }
}

fun verifyCacheImageButton(): Int {
    return if (ModuleHawk.contains(MovieKeys.FAVORITES)) {
        com.amandaluz.ui.R.drawable.ic_favorite_button_selected
    } else {
        com.amandaluz.ui.R.drawable.ic_favorite_button_unselected
    }
}

fun verifyImageButton(movie: Result, list: MutableList<Result>): Int {
    return if (list.contains(movie)) {
        com.amandaluz.ui.R.drawable.ic_favorite_button_selected
    } else {
        com.amandaluz.ui.R.drawable.ic_favorite_button_unselected
    }
}

fun validatePoster(movie: Result) =
    if (movie.poster_path.isNullOrEmpty()) linkPathNull()
    else movie.poster_path

fun validateDescription(description: String, context: Context) =
    if (description.isNullOrEmpty()) context.getString(R.string.label_indisponible)
    else description
