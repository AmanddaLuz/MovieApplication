package com.amandaluz.movieapplication.view.home.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.amandaluz.core.util.*
import com.amandaluz.core.util.recyclerview.GridRecycler
import com.amandaluz.core.util.Status
import com.amandaluz.movieapplication.R
import com.amandaluz.movieapplication.databinding.FragmentHomeBinding
import com.amandaluz.movieapplication.di.MovieComponent
import com.amandaluz.movieapplication.di.TrailerComponent
import com.amandaluz.movieapplication.util.*
import com.amandaluz.movieapplication.view.adapter.MovieAdapter
import com.amandaluz.movieapplication.view.viewmodel.MovieViewModel
import com.amandaluz.network.model.movie.Result
import com.amandaluz.network.model.trailer.ResultTrailer
import com.amandaluz.ui.customView.BottomSheetDetail
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var myAdapter: MovieAdapter
    private var movieList = mutableListOf<Result>()
    private var trailerList = mutableListOf<ResultTrailer>()
    private var favoriteList = mutableListOf<Result>()
    private lateinit var movieResponse: List<Result>
    private lateinit var trailerResponse: List<ResultTrailer>
    private var page: Int = 1
    private val viewModel by viewModel<MovieViewModel>()
    private var checkItem: Boolean = false
    private var isConnect: Boolean = false
    private var count = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MovieComponent.inject()
        TrailerComponent.inject()
        init()
        cacheOrResponse()
    }

    override fun onStart() {
        super.onStart()
        swipeRefresh()
    }

    private fun swipeRefresh() {
        binding.swipe.setOnRefreshListener {
            cacheOrResponse()
            binding.swipe.isRefreshing = false
        }
    }

    private fun init() {
        hasConnection()
        observeVMEvents()
        verifyFavoriteCache()
    }

    private fun hasConnection() {
        viewModel.hasInternet(context)
    }

    private fun getPopularMovie() {
        viewModel.getPopularMovies(apikey(), language(), page)
    }

    private fun verifyFavoriteCache() {
        verifyCacheFavorites({ favoriteList = getFavoritesCache() as MutableList<Result> }, {
            Timber.tag(
                getString(
                    R.string.first_access
                )
            )
        })
    }

    private fun observeVMEvents() {
        viewModel.isConnected.observe(viewLifecycleOwner) {
            Timber.tag(getString(R.string.check_connection)).i(it.toString())
            isConnect = it
        }
        viewModel.response.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) return@observe
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        setResponseMovie(response)
                    }
                }
                Status.LOADING -> {
                    isLoading(it.loading)
                }
                Status.ERROR -> {
                    toast("ERRO NO GET MOVIE")
                }
            }
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
                        toast(getString(R.string.toast_error))
                }
            }
        }
    }

    private fun cacheOrResponse() {
        hasConnection()
        if (hasInternet(context)) {
            movieResponse = getMovieCache()
            getPopularMovie()
        }
        else {
            verifyCacheMovies(
                { movieResponse = getMovieCache() },
                {
                    movieResponse = listOf()
                    toast("NÃO TEM NADAAAAAAAAAAAAAAA")
                }
            )
            if (count == 0){
                openDialogConnection({cacheOrResponse()}, {}, childFragmentManager)
                count ++
            } else{
                openDialogError({cacheOrResponse()}, childFragmentManager)
            }
            setLabelTryAgain()
        }
        recycler()
    }

    private fun setLabelTryAgain() {
        hasConnection()
        if (isConnect) {
            binding.labelConnection.visibility = View.GONE
            binding.swipe.isRefreshing = false
        } else {
            binding.labelConnection.visibility = View.VISIBLE
            isLoading(false)
            binding.run {
                labelConnection.text = getString(R.string.try_again)
                labelConnection.visibility = View.VISIBLE
                labelConnection.setOnClickListener {
                    cacheOrResponse()
                }
            }
        }
    }

    private fun setResponseTrailer(response: List<ResultTrailer>) {
        if (response != trailerList && response.isNotEmpty()) {
            trailerList.addAll(response)
            addCacheTrailer(trailerResult = trailerList)
            trailerResponse = getTrailerCache()
            goToYoutube()
        } else {
            toast(getString(R.string.toast_indisponible_trailer))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setResponseMovie(responseMovie: List<Result>) {
        binding.labelConnection.visibility = View.GONE
        if (responseMovie != movieList) {
            movieList.addAll(responseMovie)
            addCacheMovies(moviesResult = movieList)
            movieResponse = getMovieCache()
            myAdapter.notifyDataSetChanged()
        }
    }

    private fun goToYoutube() {
        openNewTabWindow(
            "${goToYoutubeUrl()}${
                getTrailerKey(
                    isConnect,
                    trailerList,
                    trailerResponse
                )
            }", requireContext()
        )
    }

    private fun recycler() {
        binding.rvHomeFragment.apply {
            setAdapter()
            animateList()
            setHasFixedSize(true)
            adapter = myAdapter
            addOnScrollListener(endlessGridRecycler())
        }
    }

    private fun setAdapter() {
        myAdapter = MovieAdapter(
            if (isConnect) movieList
            else movieResponse
        ) { movie ->
            hasConnection()
            setLabelTryAgain()
            callBottomSheet(movie)
        }
    }

    private fun endlessGridRecycler() = GridRecycler {
        page += 1
        hasConnection()
        if (isConnect) {
            binding.labelConnection.visibility = View.GONE
            getPopularMovie()
        }
        setLabelTryAgain()
    }

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
            checkOpenTrailer(movie)
            it.dismiss()
        }
        bottomSheetDetail.buttonFavoriteAction {
            verifyAddOrRemove(movie, bottomSheetDetail)
        }
        bottomSheetDetail.setImageButton(verifyImageButton(movie, favoriteList))
        bottomSheetDetail.show(childFragmentManager, getString(R.string.create_bottom_sheet))
    }

    private fun checkOpenTrailer(movie: Result) {
        hasConnection()
        if (isConnect) viewModel.getTrailerMovies(apikey(), language(), movie.id)
        else toast(getString(R.string.connection_trailer))
    }

    private fun verifyAddOrRemove(
        movie: Result,
        bottomSheetDetail: BottomSheetDetail
    ) {
        checkItem = if (!checkItem) {
            favoriteList.add(movie)
            addCacheFavorites(favoriteList)
            bottomSheetDetail.isFavorite(true)
            true
        } else {
            favoriteList.remove(movie)
            addCacheFavorites(favoriteList)
            bottomSheetDetail.isFavorite(false)
            false
        }
    }

    private fun isLoading(it: Boolean) {
        if (it) {
            setLoading()
        } else {
            closeLoading()
        }
    }

    private fun closeLoading() {
        binding.loadingFragment.visibility = View.GONE
    }

    private fun setLoading() {
        binding.loadingFragment.visibility = View.VISIBLE
    }
}