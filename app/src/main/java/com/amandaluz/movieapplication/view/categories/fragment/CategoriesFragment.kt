package com.amandaluz.movieapplication.view.categories.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.amandaluz.core.util.*
import com.amandaluz.core.util.recyclerview.LinearRecycler
import com.amandaluz.movieapplication.R
import com.amandaluz.movieapplication.databinding.FragmentCategoriesBinding
import com.amandaluz.movieapplication.di.CategoryComponent
import com.amandaluz.movieapplication.util.addCacheTrailer
import com.amandaluz.movieapplication.util.getHomeTrailerKey
import com.amandaluz.movieapplication.util.getTrailerCache
import com.amandaluz.movieapplication.view.adapter.CategoryAdapter
import com.amandaluz.movieapplication.view.categories.viewmodel.CategoriesViewModel
import com.amandaluz.network.model.category.CategoryItem
import com.amandaluz.network.model.movie.Result
import com.amandaluz.network.model.trailer.ResultTrailer
import com.amandaluz.ui.customView.BottomSheetDetail
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var myAdapter: CategoryAdapter
    private val bottomSheetDetail = BottomSheetDetail()
    private var trailerList = mutableListOf<ResultTrailer>()
    private lateinit var trailerResponse: List<ResultTrailer>
    private var categoryList = mutableListOf<CategoryItem>()
    private val viewModel by viewModel<CategoriesViewModel>()
    private var page: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkConnection()
    }

    private fun checkConnection(){
        if (hasInternet(context)){
            binding.labelConnection.visibility = View.GONE
            init()
        }
        else{
            binding.labelConnection.visibility = View.VISIBLE
        }
    }

    private fun init() {
        CategoryComponent.inject()
        getResponseMovie()
        observeVMEvents()
        recycler()
    }

    private fun getResponseMovie() {
        viewModel.getPopularMovies(apikey(), language(), page)
    }

    private fun getUpcoming(){
        viewModel.getUpComing(page)
    }

    private fun getTopRate(){
        viewModel.getTopRate(page)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeVMEvents() {
        viewModel.response.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) return@observe
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        if (response != categoryList) {

                            categoryList.add(CategoryItem(getString(R.string.category_populary), response))
                        }
                        getUpcoming()
                    }
                }
                Status.LOADING -> { isLoading(it.loading) }
                Status.ERROR -> {
                    toast(getString(R.string.toast_error))
                }
            }
        }
        viewModel.coming.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) return@observe
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        if (response.results != categoryList) {

                            categoryList.add(CategoryItem(getString(R.string.category_upcoming), response.results))
                        }
                        getTopRate()
                    }
                }
                Status.LOADING -> { }
                Status.ERROR -> {
                    toast(getString(R.string.toast_error))
                }
            }
        }
        viewModel.rate.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) return@observe
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        if (response.results != categoryList) {

                            categoryList.add(CategoryItem(getString(R.string.category_top_rates), response.results))
                            myAdapter.notifyDataSetChanged()
                        }
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
    }

    private fun setResponseTrailer(response: List<ResultTrailer>) {
        if (response != trailerList && response.isNotEmpty()) {
            trailerList.addAll(response)
            actionTrailerMovie()
            goToYoutube()
        } else {
            toast(getString(R.string.toast_indisponible_trailer))
        }
    }

    private fun actionTrailerMovie() {
        addCacheTrailer(trailerResult = trailerList)
        trailerResponse = getTrailerCache()
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
        binding.rvHomeCategories.apply {
            setAdapter()
            animateList()
            setHasFixedSize(true)
            adapter = myAdapter
            addOnScrollListener(endlessGridRecycler())
        }
    }

    private fun setAdapter() {
        myAdapter = CategoryAdapter(categoryList) { movie ->
            toast(movie.title)
            callBottomSheet(movie)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun callBottomSheet(movie: Result) {
        com.amandaluz.movieapplication.util.callBottomSheet(
            bottomSheetDetail,
            movie,
            requireContext(),
            { if (hasInternet(context)) getTrailer(movie)
            else toast(getString(R.string.connection_trailer))},
            {

            },
            com.amandaluz.ui.R.drawable.ic_stars_rating,
            childFragmentManager,
            getString(R.string.create_bottom_sheet)
        )
    }

    private fun getTrailer(movie: Result) {
        viewModel.getTrailerMovies(apikey(), language(), movie.id)
    }

    private fun endlessGridRecycler() = LinearRecycler {
        page += 1
        getResponseMovie()
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

}
