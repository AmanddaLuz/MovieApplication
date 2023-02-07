package com.amandaluz.movieapplication.view.rating

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.amandaluz.movieapplication.databinding.FragmentStatementBinding
import com.amandaluz.ui.R
import com.amandaluz.ui.customView.flipper.model.posterList
import com.amandaluz.ui.customView.gesture.GestureListener
import com.amandaluz.network.model.movie.Result

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



        setLayout()
        swipeAction()
        getMovieByIntent()
    }

    private fun getMovieByIntent() {
        activity?.let {
            movie = it.intent.getParcelableExtra<Result>("MOVIE") as Result
        }
    }

    private fun setLayout() {
        with(binding.flipper) {
            setList(posterList())
            setLayout()
            addDots()
        }
    }

    private fun addDots() {
        binding.dots.setLayout(posterList().size, binding.flipper.displayedChild)
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