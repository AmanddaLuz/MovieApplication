package com.amandaluz.movieapplication.view.categories.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amandaluz.core.BuildConfig.API_KEY
import com.amandaluz.core.util.Status
import com.amandaluz.core.util.connection.hasInternet
import com.amandaluz.core.util.extensions.toast
import com.amandaluz.core.util.openlink.openNewTabWindow
import com.amandaluz.core.util.recycler.animateList
import com.amandaluz.core.util.url.language
import com.amandaluz.core.util.url.openYoutube
import com.amandaluz.movieapplication.R
import com.amandaluz.movieapplication.databinding.FragmentCategoriesBinding
import com.amandaluz.movieapplication.di.CategoryComponent
import com.amandaluz.movieapplication.util.bottomsheet.getHomeTrailerKey
import com.amandaluz.movieapplication.util.cache.addCacheTrailer
import com.amandaluz.movieapplication.util.cache.getTrailerCache
import com.amandaluz.movieapplication.view.adapter.CategoryAdapter
import com.amandaluz.movieapplication.view.categories.viewmodel.CategoriesViewModel
import com.amandaluz.network.model.category.CategoryItem
import com.amandaluz.network.model.movie.Result
import com.amandaluz.network.model.trailer.ResultTrailer
import com.amandaluz.ui.customView.bottomsheet.BottomSheetDetail
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    private lateinit var myAdapter: CategoryAdapter
    private lateinit var binding: FragmentCategoriesBinding
    private val bottomSheetDetail = BottomSheetDetail()
    private val popularList = mutableListOf<Result>()

    private val viewModel by viewModel<CategoriesViewModel>()
    private val topList = mutableListOf<Result>()
    private val upList = mutableListOf<Result>()
    private val categoryList = mutableListOf<CategoryItem>()

    private val trailerList = mutableListOf<ResultTrailer>()
    private var trailerResponse: List<ResultTrailer>? = null

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentCategoriesBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CategoryComponent.inject()

        if (hasInternet(requireContext())) {
            setupListeners()
            refreshData()
            binding.labelConnection.visibility = View.GONE
        } else {
            toast(getString(R.string.connection_trailer))
            binding.labelConnection.visibility = View.VISIBLE
        }

        setObservers()
    }

    private fun setObservers() {
        viewModel.response.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    val list = result.data
                    popularList.clear()
                    if (list != null) {
                        popularList.addAll(list)
                    }
                    recycler()
                }
                Status.ERROR -> {
                    toast(getString(R.string.toast_error))
                }
                Status.LOADING -> {
                    isLoading(result.loading)
                }
            }
        }

        viewModel.rate.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    val list = result.data
                    topList.clear()
                    list?.results?.let { topList.addAll(it) }
                    recycler()
                }
                Status.ERROR -> {
                    toast(getString(R.string.toast_error))
                }
                Status.LOADING -> {
                    isLoading(result.loading)
                }
            }
        }

        viewModel.coming.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    val list = result.data
                    upList.clear()
                    list?.results?.let { upList.addAll(it) }
                    recycler()
                }
                Status.ERROR -> {
                    toast(getString(R.string.toast_error))
                }
                Status.LOADING -> {
                    isLoading(result.loading)
                }
            }
        }

        viewModel.responseTrailer.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    val response = result.data
                    if (response != null) {
                        setResponseTrailer(response)
                    }
                }
                Status.ERROR -> {
                    toast(getString(R.string.toast_error))
                }
                Status.LOADING -> {
                    isLoading(result.loading)
                }
            }
        }
    }

    private fun setupListeners() {
        binding.swipe.setOnRefreshListener { refreshData() }
    }

    private fun refreshData() {
        viewModel.apply {
            getPopularMovies(API_KEY, language(), page = 1)
            getTopRate(API_KEY, page = 1)
            getUpComing(API_KEY, page = 1)
        }
        binding.swipe.isRefreshing = false
    }

    private fun setResponseTrailer(response: List<ResultTrailer>) {
        if (response != trailerList && response.isNotEmpty()) {
            trailerList.addAll(response)
            actionTrailerMovie()
            openYoutubeLink()
        } else {
            toast(getString(R.string.toast_indisponible_trailer))
        }
    }

    private fun actionTrailerMovie() {
        addCacheTrailer(trailerResult = trailerList)
        trailerResponse = getTrailerCache()
    }

    private fun openYoutubeLink() {
        val youtubeUrl = "${openYoutube()}${trailerResponse?.let {
            getHomeTrailerKey(hasInternet(context), trailerList,
                it
            )
        }}"
        openNewTabWindow(youtubeUrl, requireContext())
    }

    private fun recycler() {
        setAdapter()
        binding.rvHomeCategories.apply {
            animateList()
            setHasFixedSize(true)
            adapter = myAdapter
        }
    }

    private fun setAdapter() {
        categoryList.clear()

        val categoryNames = categoryList.map { it.title }
        if (!categoryNames.contains("Lançamentos")) {
            categoryList.add(CategoryItem("Lançamentos", upList))
        }
        if (!categoryNames.contains("Melhor Classificados")) {
            categoryList.add(CategoryItem("Melhor Classificados", topList))
        }
        if (!categoryNames.contains("Populares")) {
            categoryList.add(CategoryItem("Populares", popularList))
        }
        myAdapter = CategoryAdapter(categoryList) { movie ->
            callBottomSheet(movie)
        }
    }

    private fun callBottomSheet(movie : Result) {
        com.amandaluz.movieapplication.util.bottomsheet.callBottomSheet(
            bottomSheetDetail ,
            movie ,
            requireContext() ,
            {
                if (hasInternet(context)) getTrailer(movie)
                else toast(getString(R.string.connection_trailer))
            } ,
            {
                findNavController().navigate(
                    R.id.action_categoriesFragment_to_ratingFragment ,
                    Bundle().apply {
                        putParcelable("MOVIE" , movie)
                    })
                bottomSheetDetail.dismiss()
            } ,
            com.amandaluz.ui.R.drawable.ic_stars_rating ,
            childFragmentManager ,
            getString(R.string.create_bottom_sheet)
        )
    }

    private fun getTrailer(movie : Result) {
        movie.id?.let { id -> viewModel.getTrailerMovies(API_KEY , language() , id) }
    }

    private fun isLoading(loading: Boolean) {
        binding.loadingFragment.visibility = if (loading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        CategoryComponent.unload()
    }
}
