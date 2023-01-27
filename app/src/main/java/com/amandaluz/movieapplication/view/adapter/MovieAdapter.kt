package com.amandaluz.movieapplication.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amandaluz.movieapplication.databinding.MovieItemBinding
import com.amandaluz.network.model.movie.Result
import com.bumptech.glide.Glide

class MovieAdapter(

    private val moviesList: List<Result>,
    private val itemClick: ((item: Result) -> Unit)
) : RecyclerView.Adapter<MovieAdapter.MyViewHolder>() {

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
                val initPath = "https://image.tmdb.org/t/p/w500"
                val pathNull = "https://www.unideanellemani.it/wp-content/uploads/2020/01/placeholder-1024x683.png"
                val popularityRate = "Popularidade: ${movie.popularity.toInt()}"
                tvPopularityDateItem.text = popularityRate

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
            pathNull: String,
            initPath: String
        ) = if (movie.poster_path.isNullOrEmpty()) pathNull
        else initPath.plus(movie.poster_path)
    }
}