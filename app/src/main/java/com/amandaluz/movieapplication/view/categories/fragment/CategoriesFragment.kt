package com.amandaluz.movieapplication.view.categories.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.amandaluz.core.util.Status
import com.amandaluz.core.util.apikey
import com.amandaluz.core.util.language
import com.amandaluz.core.util.toast
import com.amandaluz.movieapplication.R
import com.amandaluz.movieapplication.databinding.FragmentCategoriesBinding
import com.amandaluz.movieapplication.databinding.FragmentFavoriteBinding
import com.amandaluz.movieapplication.di.MovieComponent
import com.amandaluz.movieapplication.view.adapter.CategoryAdapter
import com.amandaluz.movieapplication.view.adapter.MovieAdapter
import com.amandaluz.movieapplication.view.viewmodel.MovieViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var myAdapter: CategoryAdapter
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

    private fun init(){
        MovieComponent.inject()
    }

    private fun getResponseMovie(){
        viewModel.getPopularMoviesResponse(apikey(), language(), page)
    }

    private fun observeVMEvents() {
        viewModel.response.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) return@observe
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->

                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    toast("ERRO NO GET MOVIE")
                }
            }
        }

    }
}