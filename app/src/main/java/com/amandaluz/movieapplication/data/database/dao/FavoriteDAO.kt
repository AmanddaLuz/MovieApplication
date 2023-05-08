package com.amandaluz.movieapplication.data.database.dao

import com.amandaluz.network.model.movie.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FavoriteDAO {
        private var userId : String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        private val db = Firebase.database
        val myRef = db.getReference("favorites").child(userId)
        fun getAllFavoritesById(favoriteList : MutableList<Result> , onRequestDb : OnRequestDb? = null) {
            myRef.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot : DataSnapshot) {
                        snapshot.children.forEach {
                            it.getValue(Result::class.java)?.let { favorites ->
                                if (!favoriteList.contains(favorites)) {
                                    favoriteList.add(favorites)
                                }
                            }
                        }
                        onRequestDb?.onCompleted()
                    }

                    override fun onCancelled(error : DatabaseError) {
                        TODO("Not yet implemented")
                    }
                }
            )
        }
}