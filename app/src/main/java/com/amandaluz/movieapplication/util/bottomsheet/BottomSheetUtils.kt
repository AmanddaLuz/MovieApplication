package com.amandaluz.movieapplication.util.bottomsheet

import android.content.Context
import androidx.fragment.app.FragmentManager
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
        viewTargetPoster(movie.posterPath ?: "")
        viewTargetDetail(movie.posterPath ?: "")
        setTitle(movie.title ?: "")
        setNota(buildString {
            append(context.getString(R.string.average))
            append(movie.voteAverage)
        })
        setDescription(buildString {
            append(context.getString(R.string.votes))
            append(movie.voteCount)
        })
        setDetail(movie.overview ?: "")
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
