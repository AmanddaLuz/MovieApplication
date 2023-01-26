package com.amandaluz.core.util.recyclerview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LinearRecycler(private val loadList: () -> Unit) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dx > 0) { //check for scroll down
            val visibleItemCount = recyclerView.layoutManager?.childCount ?: 0
            val totalItemCount = recyclerView.layoutManager?.itemCount ?: 0
            val pastVisibleItems =
                (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                loadList.invoke()
            }
        }
    }
}
