package com.amandaluz.movieapplication.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amandaluz.ui.dialog.popularity
import com.amandaluz.core.util.url.initPath
import com.amandaluz.movieapplication.databinding.MovieItemBinding
import com.amandaluz.network.model.movie.Result
import com.bumptech.glide.Glide

class MovieAdapter(

    private var moviesList: List<Result> ,
    private val itemClick: ((item: Result) -> Unit)
) : RecyclerView.Adapter<MovieAdapter.MyViewHolder>() {

    fun updateList(newList: List<Result>) {
        moviesList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = MovieItemBinding.inflate(view, parent, false)
        return MyViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movies = moviesList[position]
        holder.bindView(movies)
    }

    override fun getItemCount(): Int = moviesList.size

    class MyViewHolder(
        private val binding: MovieItemBinding,
        private val itemClick: (item: Result) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(movie: Result) {
            binding.run {
                val initPath = initPath()
                val pathNull = com.amandaluz.ui.R.drawable.placeholder
                val popularityRate = "${popularity()}${movie.popularity?.toInt()}"
                tvPopularityItem.text = popularityRate

                Glide.with(itemView)
                    .load(validateImagePoster(movie, pathNull, initPath))
                    .centerCrop()
                    .into(imageItem)
            }

            itemView.setOnClickListener {
                itemClick.invoke(movie)
            }
        }

        private fun validateImagePoster(
            movie: Result,
            pathNull: Int,
            initPath: String
        ) = if (movie.posterPath.isNullOrEmpty()) pathNull
        else initPath.plus(movie.posterPath)
    }
}