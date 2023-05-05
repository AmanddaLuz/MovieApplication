package com.amandaluz.movieapplication.view.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amandaluz.movieapplication.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var userId: String

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnConfirm.setOnClickListener {
            val name = binding.fullNameEdit.text.toString()
            val email = binding.emailEdit.text.toString()
            val password = binding.passwordEdit.text.toString()
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this , "Preencha todos os campos" , Toast.LENGTH_SHORT).show()
            } else {
                registerUser()
            }
        }
    }

    private fun registerUser(){
        val email = binding.emailEdit.text.toString()
        val password = binding.passwordEdit.text.toString()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {isRegister ->
            if(isRegister.isSuccessful){
                saveUserData()
                Toast.makeText(this , "Cadastro realizado com sucesso!" , Toast.LENGTH_SHORT).show()
            } else{
                val error: String = try {
                    throw isRegister.exception ?: Exception()
                }catch (ex: FirebaseAuthWeakPasswordException){
                    "Digite uma senha com no mínimo 6 caracteres!"
                }catch (ex: FirebaseAuthUserCollisionException){
                    "Esta conta já foi cadastrada!"
                }catch (ex: FirebaseAuthInvalidCredentialsException){
                    "Digite um e-mail válido!"
                }catch (e: Exception){
                    "Erro ao cadastrar usuário!"
                }
                Toast.makeText(this , error , Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData(){
        val name = binding.fullNameEdit.text.toString()
        val db = FirebaseFirestore.getInstance()

        val user = HashMap<String, String>()
        user["name"] = name

        userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val documentReference = db.collection("users").document(userId)
        documentReference.set(user).addOnSuccessListener {
            Timber.tag("db_success").d("Dados salvos com sucesso!")
        }.addOnFailureListener {
            Timber.tag("db_error").d("Falha ao salvar os dados!")
        }
        startActivity(Intent(this , LoginActivity::class.java))
    }
}