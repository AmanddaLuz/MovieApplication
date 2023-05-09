package com.amandaluz.movieapplication.view.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amandaluz.movieapplication.R
import com.amandaluz.movieapplication.databinding.ActivityLoginBinding
import com.amandaluz.movieapplication.util.watcher.Watcher
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val watcher = Watcher {
        binding.btnLogin.isEnabled = binding.loginUserEmailEdit.text.toString().isNotEmpty() &&
                binding.loginPasswordEdit.text.toString().isNotEmpty()
    }
    val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        goToRegister()
        handlerEnableButton()
        login()
    }

    private fun login() {
        binding.btnLogin.setOnClickListener {
            authUser()
        }
    }

    private fun handlerEnableButton() {
        binding.run {
            loginUserEmailEdit.addTextChangedListener(watcher)
            loginPasswordEdit.addTextChangedListener(watcher)
        }
    }

    private fun goToRegister() {
        binding.loginRegisterButton.setOnClickListener {
            startActivity(Intent(this , RegisterActivity::class.java))
        }
    }

    private fun authUser() {
        val email = binding.loginUserEmailEdit.text.toString()
        val password = binding.loginPasswordEdit.text.toString()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email , password)
            .addOnCompleteListener { isUser ->
                validUser(isUser)
            }
    }

    private fun validUser(isUser : Task<AuthResult>) {
        if (isUser.isSuccessful) {
            binding.btnLogin.progress(true)
            loadingButton()
        } else {
            try { throw isUser.exception ?: Exception() } catch (e : Exception) { Timber.tag("$ERROR - $e") }
            Toast.makeText(this , getString(R.string.login_error) , Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadingButton() {
        coroutineScope.launch {
            delay(2000)
            goToUser()
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            goToUser()
        }
    }

    private fun goToUser() {
        startActivity(Intent(this@LoginActivity , UserActivity::class.java))
    }

    companion object{
        const val ERROR = "ERROR_LOGIN"
    }
}