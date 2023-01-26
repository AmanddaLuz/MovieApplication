package com.amandaluz.movieapplication.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amandaluz.movieapplication.databinding.MovieItemBinding
import com.amandaluz.network.model.movie.Result
import com.bumptech.glide.Glide

class ItemAdapter(

    private val moviesList: List<Result>,
    private val itemClick: ((item: Result) -> Unit)
) : RecyclerView.Adapter<ItemAdapter.MyViewHolder>() {

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
            if (movie.adult){
                binding.run {
                    val initPath = "https://image.tmdb.org/t/p/w500"
                    val popularityRate = "Popularidade: ${movie.popularity.toInt()}"
                    tvPopularityDateItem.text = popularityRate
                    Glide.with(itemView)
                        .load(initPath.plus(movie.poster_path))
                        .centerCrop()
                        .into(imageItem)
                }

                itemView.setOnClickListener {
                    itemClick.invoke(movie)
                }
            }else{
                val initPath = "https://image.tmdb.org/t/p/w500"
                Glide.with(itemView)
                    .load(initPath.plus(movie.poster_path))
                    .centerCrop()
                    .into(binding.imageItem)
            }
        }
    }
}