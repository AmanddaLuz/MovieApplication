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
import com.amandaluz.movieapplication.databinding.FragmentCategoriesBinding
import com.amandaluz.movieapplication.di.MovieComponent
import com.amandaluz.movieapplication.view.adapter.CategoryAdapter
import com.amandaluz.movieapplication.view.viewmodel.MovieViewModel
import com.amandaluz.network.model.category.CategoryItem
import com.amandaluz.network.model.movie.Result
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var myAdapter: CategoryAdapter
    private var categoryList = mutableListOf<CategoryItem>()
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
        viewModel.getPopularMovies(apikey(), language(), page)
        viewModel.getTopRate(page)
        viewModel.getUpComing(page)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeVMEvents() {
        viewModel.response.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) return@observe
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        if (response != categoryList) {

                            categoryList.add(CategoryItem("Populares", response))
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
        viewModel.rate.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) return@observe
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        if (response.results != categoryList) {

                            categoryList.add(CategoryItem("Top Rates", response.results))
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
        viewModel.coming.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) return@observe
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        if (response.results != categoryList) {

                            categoryList.add(CategoryItem("UpComings", response.results))
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
        myAdapter = CategoryAdapter(categoryList) { movie ->

        }
    }

    private fun endlessGridRecycler() = LinearRecycler {
        page += 1
        getResponseMovie()
    }

}
