package com.amandaluz.movieapplication.view.categories.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.amandaluz.core.util.*
import com.amandaluz.core.util.recyclerview.GridRecycler
import com.amandaluz.core.util.recyclerview.LinearRecycler
import com.amandaluz.movieapplication.R
import com.amandaluz.movieapplication.databinding.FragmentCategoriesBinding
import com.amandaluz.movieapplication.databinding.FragmentFavoriteBinding
import com.amandaluz.movieapplication.di.MovieComponent
import com.amandaluz.movieapplication.util.addCacheMovies
import com.amandaluz.movieapplication.util.getMovieCache
import com.amandaluz.movieapplication.view.adapter.CategoryAdapter
import com.amandaluz.movieapplication.view.adapter.MovieAdapter
import com.amandaluz.movieapplication.view.viewmodel.MovieViewModel
import com.amandaluz.network.model.movie.MovieResponse
import com.amandaluz.network.model.movie.Result
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var myAdapter: CategoryAdapter
    private var movieList = mutableListOf<Result>()
    private val viewModel by viewModel<MovieViewModel>()
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

        init()
    }

    private fun init() {
        MovieComponent.inject()
        getResponseMovie()
        observeVMEvents()
        recycler()
    }

    private fun getResponseMovie() {
        viewModel.getPopularMoviesResponse(apikey(), language(), page)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeVMEvents() {
        viewModel.movies.observe(viewLifecycleOwner) {
            //if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) return@observe
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {response->
                        if (response.results != movieList){
                            movieList.addAll(response.results)
                            myAdapter.notifyDataSetChanged()
                        }
                    }
                }
                Status.LOADING -> {
                    toast("MOVIE RESPONSE")
                }
                Status.ERROR -> {
                    toast("ERRO NO GET MOVIE RESPONSE")
                }
            }
        }

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
        myAdapter = CategoryAdapter(movieList) { movie ->

        }
    }

    private fun endlessGridRecycler() = LinearRecycler {
        page += 1
        getResponseMovie()
    }

}