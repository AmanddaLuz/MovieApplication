package com.amandaluz.movieapplication.data.database.dao

import android.content.Context
import android.widget.Toast
import com.amandaluz.network.model.movie.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class FavoriteDAO {
    private var userId : String = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val db = Firebase.database
    val myRef = db.getReference("favorites").child(userId)

    fun getAllFavoritesById(
        favoriteList : MutableList<Result> ,
        onRequestDb : OnRequestDb? = null
    ) {
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

    fun insertUser(email : String , password : String , name : String , context : Context) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email , password)
            .addOnCompleteListener { isRegister ->
                if (isRegister.isSuccessful) {
                    saveUserData(name)
                    Toast.makeText(context , "Cadastro realizado com sucesso!" , Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val error : String = try {
                        throw isRegister.exception ?: Exception()
                    } catch (ex : FirebaseAuthWeakPasswordException) {
                        "Digite uma senha com no mínimo 6 caracteres!"
                    } catch (ex : FirebaseAuthUserCollisionException) {
                        "Esta conta já foi cadastrada!"
                    } catch (ex : FirebaseAuthInvalidCredentialsException) {
                        "Digite um e-mail válido!"
                    } catch (e : Exception) {
                        "Erro ao cadastrar usuário!"
                    }
                    Toast.makeText(context , error , Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserData(name : String) {
        val db = FirebaseFirestore.getInstance()

        val user = HashMap<String , String>()
        user["name"] = name


        userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

        val documentReference = db.collection("users").document(userId)

        documentReference.set(user).addOnSuccessListener {
            Timber.tag("db_success").d("Dados salvos com sucesso!")
        }.addOnFailureListener {
            Timber.tag("db_error").d("Falha ao salvar os dados!")
        }
    }
}