package com.amandaluz.movieapplication.view.favorite.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.amandaluz.core.BuildConfig.API_KEY
import com.amandaluz.core.util.*
import com.amandaluz.core.util.connection.hasInternet
import com.amandaluz.core.util.extensions.toast
import com.amandaluz.core.util.openlink.openNewTabWindow
import com.amandaluz.core.util.recycler.animateList
import com.amandaluz.core.util.url.goToYoutubeUrl
import com.amandaluz.core.util.url.language
import com.amandaluz.movieapplication.R
import com.amandaluz.movieapplication.data.database.dao.FavoriteDAO
import com.amandaluz.movieapplication.data.database.dao.OnRequestDb
import com.amandaluz.movieapplication.databinding.FragmentFavoriteBinding
import com.amandaluz.movieapplication.di.FavoriteComponent
import com.amandaluz.movieapplication.util.*
import com.amandaluz.movieapplication.util.bottomsheet.verifyImageButton
import com.amandaluz.movieapplication.view.adapter.MovieAdapter
import com.amandaluz.movieapplication.view.favorite.viewmodel.FavoriteViewModel
import com.amandaluz.network.model.movie.Result
import com.amandaluz.network.model.trailer.ResultTrailer
import com.amandaluz.ui.customView.bottomsheet.BottomSheetDetail
import com.amandaluz.ui.dialog.openDialogNoConnection
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private var favoriteListSave = mutableListOf<Result>()
    private var trailerList = mutableListOf<ResultTrailer>()
    private lateinit var binding : FragmentFavoriteBinding
    private lateinit var myAdapter : MovieAdapter
    private val bottomSheetDetail = BottomSheetDetail()
    private val viewModel by viewModel<FavoriteViewModel>()

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentFavoriteBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        checkLabelConnection()
        init()
        observeVMEvents()
        setAdapter()
    }

    private fun init() {
        FavoriteComponent.inject()
    }

    override fun onStart() {
        super.onStart()
        swipeRefresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        FavoriteComponent.unload()
    }

    private fun swipeRefresh() {
        binding.swipe.setOnRefreshListener {
            checkLabelConnection()
            setRecycler()
            binding.swipe.isRefreshing = false
        }
    }

    private fun checkLabelConnection() {
        if (hasInternet(context)) binding.labelConnection.visibility = View.GONE
        else binding.labelConnection.visibility = View.VISIBLE
    }

    private fun observeVMEvents() {
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
                    requireContext().openDialogNoConnection({} , {} , childFragmentManager)
                }
            }
        }
    }

    private fun setResponseTrailer(response : List<ResultTrailer>) {
        if (response.isNotEmpty()) {
            trailerList = response as MutableList<ResultTrailer>
            goToYoutube()
        } else {
            toast(getString(R.string.toast_indisponible_trailer))
        }
    }

    private fun goToYoutube() {
        openNewTabWindow("${goToYoutubeUrl()}${getTrailerKey()}" , requireContext())
    }

    private fun getTrailerKey() : String? {
        var key : String? = null
        trailerList.forEach {
            key = it.key
        }
        return key
    }

    private fun setAdapter() {
        FavoriteDAO().getAllFavoritesById(favoriteListSave , object : OnRequestDb {
            override fun onCompleted() {
                setLabelNoFavorites()
                myAdapter = MovieAdapter(favoriteListSave) { movie ->
                    callBottomSheet(movie)
                }
                setRecycler()
            }
        })
    }

    private fun setRecycler() {
        binding.rvHomeFragment.apply {
            animateList()
            setHasFixedSize(true)
            adapter = myAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun callBottomSheet(movie : Result) {
        com.amandaluz.movieapplication.util.bottomsheet.callBottomSheet(
            bottomSheetDetail ,
            movie ,
            requireContext() ,
            { getTrailer(movie) } ,
            {
                removeFavorite(movie)
                bottomSheetDetail.isFavorite(false)
                myAdapter.notifyDataSetChanged()
                bottomSheetDetail.dismiss()
            } ,
            verifyImageButton(movie, favoriteListSave) ,
            childFragmentManager ,
            getString(R.string.create_bottom_sheet)
        )
    }

    private fun getTrailer(movie : Result) {
        movie.id?.let { id -> viewModel.getTrailerMovies(API_KEY , language() , id) }
    }

    private fun removeFavorite(movie : Result) {
        favoriteListSave.remove(movie)
        FavoriteDAO().myRef.setValue(favoriteListSave)
        setLabelNoFavorites()
    }

    private fun setLabelNoFavorites() {
        if (favoriteListSave.isEmpty()) binding.labelEmptyList.visibility = View.VISIBLE
        else {
            binding.labelEmptyList.visibility = View.GONE
        }
    }
}