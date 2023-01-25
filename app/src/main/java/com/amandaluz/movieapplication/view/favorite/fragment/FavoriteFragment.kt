package com.amandaluz.movieapplication.view.favorite.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.amandaluz.core.util.*
import com.amandaluz.movieapplication.R
import com.amandaluz.movieapplication.databinding.FragmentFavoriteBinding
import com.amandaluz.movieapplication.di.TrailerComponent
import com.amandaluz.movieapplication.util.*
import com.amandaluz.movieapplication.view.adapter.MovieAdapter
import com.amandaluz.movieapplication.view.viewmodel.MovieViewModel
import com.amandaluz.network.model.movie.Result
import com.amandaluz.network.model.trailer.ResultTrailer
import com.amandaluz.ui.customView.BottomSheetDetail
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class FavoriteFragment : Fragment() {

    private var favoriteList: List<Result> = listOf()
    private var favoriteListSave = mutableListOf<Result>()
    private var trailerList = mutableListOf<ResultTrailer>()
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var myAdapter: MovieAdapter
    private var isConnect: Boolean = false
    private val viewModel by viewModel<MovieViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkConnection()
        observeVMEvents()
        setAdapter()
        setRecycler()
        init()
    }

    override fun onStart() {
        super.onStart()
        swipeRefresh()
    }

    private fun swipeRefresh() {
        binding.swipe.setOnRefreshListener {
            checkConnection()
            setRecycler()
            binding.swipe.isRefreshing = false
        }
    }

    private fun isConnection(isConnection: Boolean){
        if (isConnection) binding.labelConnection.visibility = View.GONE
        else binding.labelConnection.visibility = View.VISIBLE
    }

    private fun init(){
        TrailerComponent.inject()
    }

    private fun checkConnection() {
        viewModel.hasInternet(context)
    }

    private fun observeVMEvents(){
        viewModel.isConnected.observe(viewLifecycleOwner){
            Timber.tag("CONNECTION").i(it.toString())
            isConnect = it
            isConnection(it)
        }
        viewModel.responseTrailer.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) return@observe
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        setResponseTrailer(response)
                    }
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    openDialogNoConnection({},{}, childFragmentManager)
                }
            }
        }
    }

    private fun setResponseTrailer(response: List<ResultTrailer>) {
        if (response.isNotEmpty()) {
            trailerList = response as MutableList<ResultTrailer>
            goToYoutube()
        } else {
            toast(getString(R.string.toast_indisponible_trailer))
        }
    }

    private fun goToYoutube() {
        openNewTabWindow("${goToYoutubeUrl()}${getTrailerKey()}", requireContext())
    }

    private fun getTrailerKey(): String?{
        var key: String? = null
        trailerList.forEach {
            key = it.key
        }
        return key
    }

    private fun setAdapter() {
        setLabelNoFavorites()
        verifyCacheFavorites(
            { favoriteListSave = getFavoritesCache() as MutableList<Result> },
            { setLabelNoFavorites() })
        myAdapter = MovieAdapter(favoriteListSave) {
            checkConnection()
            callBottomSheet(it)
        }
    }

    private fun setRecycler() {
        setAdapter()
        binding.rvHomeFragment.apply {
            animateList()
            setHasFixedSize(true)
            adapter = myAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun callBottomSheet(movie: Result) {
        val bottomSheetDetail = BottomSheetDetail()
        bottomSheetDetail.viewTargetDetail(movie.poster_path)
        bottomSheetDetail.viewTargetPoster(movie.poster_path)
        bottomSheetDetail.setTitle(movie.title)
        bottomSheetDetail.setNota("Nota: ${movie.vote_average}")
        bottomSheetDetail.setDescription("Votos: ${movie.vote_count}")
        bottomSheetDetail.setDetail(movie.overview)
        bottomSheetDetail.buttonCloseAction { it.dismiss() }
        bottomSheetDetail.buttonConfirmAction {
            getTrailer(movie)
            it.dismiss()
        }
        bottomSheetDetail.buttonFavoriteAction {
            removeFavorite(movie)
            bottomSheetDetail.isFavorite(false)
            myAdapter.notifyDataSetChanged()
            it.dismiss()
        }
        bottomSheetDetail.setImageButton(verifyCacheImageButton())
        bottomSheetDetail.show(childFragmentManager, "BOTTOM_SHEET - DETAIL")
    }

    private fun getTrailer(movie: Result) {
        viewModel.getTrailerMovies(apikey(), language(), movie.id)
    }

    private fun removeFavorite(movie: Result) {
        favoriteListSave.remove(movie)
        addCacheFavorites(favoriteListSave)
        setLabelNoFavorites()
    }

    private fun setLabelNoFavorites() {
        if (favoriteListSave.isEmpty()) binding.labelEmptyList.visibility = View.VISIBLE
        else {
            binding.labelEmptyList.visibility = View.GONE
        }
    }
}