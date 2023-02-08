package com.amandaluz.movieapplication.view.rating

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amandaluz.movieapplication.databinding.FragmentStatementBinding
import com.amandaluz.network.model.domain.MapperResultToPoster
import com.amandaluz.network.model.domain.poster
import com.amandaluz.network.model.movie.Result
import com.amandaluz.ui.R
import com.amandaluz.ui.customView.gesture.GestureListener

class StatementsFragment : Fragment() {
    private lateinit var binding: FragmentStatementBinding
    private lateinit var movie: Result

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMovieByIntent()
        receivedMovie()
        setLayout()
        goBackCategories()
        swipeAction()
    }

    private fun goBackCategories() {
        binding.tvClose.setOnClickListener { findNavController().popBackStack() }
    }

    private fun getMovieByIntent() {
        movie = arguments?.getParcelable<Result>("MOVIE") as Result
    }

    private fun setLayout() {
        with(binding.flipper) {
            setList(poster(movie))
            setLayout()
            addDots()
        }
    }

    private fun receivedMovie(){
        MapperResultToPoster.invoke(movie)
    }

    private fun addDots() {
        binding.dots.setLayout(poster(movie).size, binding.flipper.displayedChild)
    }

    private fun swipeAction() {
        val gestureDetector = GestureDetector(requireContext(), GestureListener({ goNext() }, { goPrevious() }, { addDots() }))
        setTouch(gestureDetector)
    }

    private fun goPrevious() {
        binding.flipper.run {
            this.setInAnimation(requireContext(), R.anim.slide_in_left)
            this.showPrevious()
        }
    }

    private fun goNext() {
        binding.flipper.run {
            this.setInAnimation(requireContext(), R.anim.slide_in_right)
            this.showNext()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouch(gestureDetector: GestureDetector) {
        binding.flipper.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }
}