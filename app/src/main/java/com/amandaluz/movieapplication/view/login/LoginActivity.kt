package com.amandaluz.movieapplication.view.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amandaluz.movieapplication.databinding.ActivityLoginBinding
import com.amandaluz.movieapplication.util.watcher.Watcher
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginRegisterButton.setOnClickListener {
            startActivity(Intent(this , RegisterActivity::class.java))
        }

        binding.run {
            loginUserEmailEdit.addTextChangedListener(watcher)
            loginPasswordEdit.addTextChangedListener(watcher)
        }

        binding.btnLogin.setOnClickListener {
            authUser()
        }
    }

    private val watcher = Watcher {
        binding.btnLogin.isEnabled = binding.loginUserEmailEdit.text.toString().isNotEmpty() &&
                binding.loginPasswordEdit.text.toString().isNotEmpty()
    }

    private fun authUser() {
        val email = binding.loginUserEmailEdit.text.toString()
        val password = binding.loginPasswordEdit.text.toString()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email , password)
            .addOnCompleteListener { isUser ->
                if (isUser.isSuccessful) {
                    binding.btnLogin.progress(true)
                    val coroutineScope = CoroutineScope(Dispatchers.Main)
                    coroutineScope.launch {
                        delay(3000)
                        goToHome()
                    }
                } else {
                    val error : String
                    try {
                        throw isUser.exception ?: Exception()
                    } catch (e : Exception) {
                        error = "Erro ao Logar!"
                    }
                    Toast.makeText(this , error , Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()

       val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null){
            goToHome()
        }
    }

    private fun goToHome() {
        startActivity(Intent(this@LoginActivity , UserActivity::class.java))
    }
}