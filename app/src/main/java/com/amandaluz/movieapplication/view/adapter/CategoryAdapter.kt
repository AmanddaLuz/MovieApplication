package com.amandaluz.movieapplication.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amandaluz.movieapplication.databinding.RecycleItemCategorieBinding
import com.amandaluz.network.model.category.CategoryItem
import com.amandaluz.network.model.movie.Result

class CategoryAdapter(

    private val moviesList: List<CategoryItem>,
    private val itemClick: ((item: Result) -> Unit)
) : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = RecycleItemCategorieBinding.inflate(view, parent, false)
        return MyViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movies = moviesList[position]
        holder.bindView(movies)
    }

    override fun getItemCount(): Int = moviesList.size

    class MyViewHolder(
        private val binding: RecycleItemCategorieBinding,
        private val itemClick: (item: Result) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(movie: CategoryItem) = with(binding){
            rvTitleCategories.text = movie.title
            rvHomeCategories.apply {
                animate()
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                setHasFixedSize (true)
                adapter = ItemAdapter (movie.result){
                    itemClick.invoke(it)
                }

            }
        }
    }
}