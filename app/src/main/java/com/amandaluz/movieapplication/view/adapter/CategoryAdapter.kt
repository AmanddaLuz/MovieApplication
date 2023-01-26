package com.amandaluz.movieapplication.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amandaluz.movieapplication.databinding.ActivityHomeBinding
import com.amandaluz.movieapplication.databinding.FragmentCategoriesBinding
import com.amandaluz.movieapplication.databinding.FragmentHomeBinding
import com.amandaluz.movieapplication.databinding.MovieItemBinding
import com.amandaluz.network.model.movie.MovieResponse
import com.amandaluz.network.model.movie.Result
import com.bumptech.glide.Glide

class CategoryAdapter(

    private val moviesList: List<Result>,
    private val itemClick: ((item: Result) -> Unit)
) : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = FragmentCategoriesBinding.inflate(view, parent, false)
        return MyViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movies = moviesList[position]
        holder.bindView(moviesList)
    }

    override fun getItemCount(): Int = moviesList.size

    class MyViewHolder(
        private val binding: FragmentCategoriesBinding,
        private val itemClick: (item: Result) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(movie: List<Result>) {
            with(binding.rvHomeCategories) {
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                setHasFixedSize (true)
                adapter = ItemAdapter (movie){
                    itemClick.invoke(it)
                }
            }
        }
    }
}