package com.amandaluz.movieapplication.view.home.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.amandaluz.core.BuildConfig.API_KEY
import com.amandaluz.core.util.*
import com.amandaluz.core.util.connection.hasInternet
import com.amandaluz.core.util.dialog.*
import com.amandaluz.core.util.extensions.toast
import com.amandaluz.core.util.openlink.openNewTabWindow
import com.amandaluz.core.util.recycler.animateList
import com.amandaluz.core.util.url.goToYoutubeUrl
import com.amandaluz.core.util.url.language
import com.amandaluz.movieapplication.R
import com.amandaluz.movieapplication.databinding.FragmentHomeBinding
import com.amandaluz.movieapplication.di.MovieComponent
import com.amandaluz.movieapplication.util.*
import com.amandaluz.movieapplication.util.bottomsheet.*
import com.amandaluz.movieapplication.util.cache.*
import com.amandaluz.movieapplication.view.adapter.MovieAdapter
import com.amandaluz.movieapplication.view.home.activity.HomeActivity
import com.amandaluz.movieapplication.view.home.viewmodel.MovieViewModel
import com.amandaluz.network.model.movie.Result
import com.amandaluz.network.model.trailer.ResultTrailer
import com.amandaluz.ui.customView.bottomsheet.BottomSheetDetail
import com.amandaluz.ui.recyclerview.GridRecycler
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var myAdapter: MovieAdapter
    private val bottomSheetDetail = BottomSheetDetail()
    private var movieList = mutableListOf<Result>()
    private var searchList = mutableListOf<Result>()
    private var trailerList = mutableListOf<ResultTrailer>()
    private var favoriteList = mutableListOf<Result>()
    private lateinit var movieResponse: List<Result>
    private lateinit var trailerResponse: List<ResultTrailer>
    private var page: Int = 1
    private val viewModel by viewModel<MovieViewModel>()
    private var isExists = false
    private var isSearch = false
    private var checkItem: Boolean = false
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
        init()
        setupToolbar()
        cacheOrResponse()
    }

    override fun onStart() {
        super.onStart()
        swipeRefresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        MovieComponent.unload()
    }

    private fun swipeRefresh() {
        binding.swipe.setOnRefreshListener {
            cacheOrResponse()
            binding.swipe.isRefreshing = false
        }
    }

    private fun init() {
        observeVMEvents()
        verifyFavoriteCache()
    }

    private fun getPopularMovie() {
        viewModel.getPopularMovies(API_KEY, language(), page)
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
                    toast(getString(R.string.toast_error))
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
        viewModel.search.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) return@observe
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        setResponseSearch(response)
                        recycler()
                    }
                }
                Status.LOADING -> {}
                Status.ERROR -> {}
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setResponseSearch(response: List<Result>) {
        isSearch = true
        searchList.clear()
        searchList.addAll(response)
        myAdapter.notifyDataSetChanged()
    }

    private fun cacheOrResponse() {
        if (hasInternet(context)) {
            getPopularMovie()
        } else {
            getCache()
            setTypeError()
            setReloadView()
        }
        recycler()
    }

    private fun getCache() {
        verifyCacheMovies({
            movieResponse = getMovieCache()
        },
            {
                movieResponse = listOf()
                toast(getString(R.string.welcome))
            }
        )
    }

    private fun setTypeError() {
        if (count == 0) {
            requireContext().openDialogConnection({ cacheOrResponse() }, {}, childFragmentManager)
            count++
        } else {
            requireContext().openDialogError({ cacheOrResponse() }, childFragmentManager)
            count = 0
        }
    }

    private fun setReloadView() {
        if (hasInternet(context)) {
            binding.labelConnection.visibility = View.GONE
            binding.swipe.isRefreshing = false
        } else {
            binding.labelConnection.visibility = View.VISIBLE
            isLoading(false)
            setLabelTryAgain()
        }
    }

    private fun setLabelTryAgain() {
        binding.run {
            labelConnection.text = getString(R.string.try_again)
            labelConnection.visibility = View.VISIBLE
            labelConnection.setOnClickListener {
                cacheOrResponse()
            }
        }
    }

    private fun setResponseTrailer(response: List<ResultTrailer>) {
        if (response != trailerList && response.isNotEmpty()) {
            trailerList.addAll(response)
            actionCacheTrailer()
            goToYoutube()
        } else {
            toast(getString(R.string.toast_indisponible_trailer))
        }
    }

    private fun actionCacheTrailer() {
        addCacheTrailer(trailerResult = trailerList)
        trailerResponse = getTrailerCache()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setResponseMovie(responseMovie: List<Result>) {
        binding.labelConnection.visibility = View.GONE
        if (isSearch) movieList.clear()
        if (responseMovie != movieList) {
            movieList.addAll(responseMovie)
            actionCacheMovie()
            myAdapter.notifyDataSetChanged()
        }
    }

    private fun actionCacheMovie() {
        addCacheMovies(moviesResult = movieList)
        movieResponse = getMovieCache()
    }

    private fun goToYoutube() {
        openNewTabWindow(
            "${goToYoutubeUrl()}${
                getHomeTrailerKey(
                    hasInternet(context),
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
            if (hasInternet(context) && isSearch) searchList
            else if (hasInternet(context) && !isSearch) movieList
            else movieResponse
        ) { movie ->
            setReloadView()
            callBottomSheet(movie)
        }
        isSearch = false
    }

    private fun callBottomSheet(movie: Result) {
        callBottomSheet(
            bottomSheetDetail,
            movie,
            requireContext(),
            { checkOpenTrailer(movie) },
            { verifyAddOrRemove(movie, bottomSheetDetail) },
            verifyImageButton(movie, favoriteList),
            childFragmentManager,
            getString(R.string.create_bottom_sheet)
        )
    }

    private fun endlessGridRecycler() = GridRecycler {
        page += 1
        if (hasInternet(context)) {
            binding.labelConnection.visibility = View.GONE
            getPopularMovie()
        }
        setReloadView()
    }

    private fun checkOpenTrailer(movie: Result) {
        if (hasInternet(context)) viewModel.getTrailerMovies(API_KEY, language(), movie.id)
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

    private fun isLoading(loading: Boolean) {
        if (loading) {
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

    private fun searchMovies(name: String) {
        viewModel.searchMovie(API_KEY, name, page)
    }

    private fun setupToolbar() = with(activity as HomeActivity) {
        setSupportActionBar(binding.includeToolbar.toolbarLayout)
        title = null
        if (!isExists) {
            setMenu()
        }
    }

    private fun setMenu() {
        activity?.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_toolbar, menu)
                setSearchView(menu)
                isExists = true
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        })
    }

    private fun setSearchView(menu: Menu) {
        val search = menu.findItem(R.id.search)
        val searchView = search.actionView as SearchView
        searchView.queryHint = getString(R.string.hint_pesquisar)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String): Boolean {
                if (hasInternet(context)){
                    when (newText) {
                        "" -> {

                        }
                        else -> {
                            searchMovies(newText)
                        }
                    }
                }else{
                    toast(getString(R.string.connection_trailer))
                }
                return false
            }
        })
        onCloseSearchViewAction(searchView)
    }

    private fun onCloseSearchViewAction(searchView: SearchView) {
        searchView.setOnCloseListener {
            if (isSearch) {
                getPopularMovie()
            }
            return@setOnCloseListener false
        }
    }

}