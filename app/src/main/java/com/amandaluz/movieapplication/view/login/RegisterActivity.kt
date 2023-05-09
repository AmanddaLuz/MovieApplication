package com.amandaluz.movieapplication.view.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amandaluz.movieapplication.R
import com.amandaluz.movieapplication.data.database.dao.FavoriteDAO
import com.amandaluz.movieapplication.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBtnBack()
        fillFields()
    }

    private fun actionBtnBack() {
        binding.btnBackAccount.setOnClickListener {
            goToLogin()
        }
    }

    private fun fillFields() = with(binding) {
        btnConfirm.setOnClickListener {
            val name = fullNameEdit.text.toString()
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@RegisterActivity , getString(R.string.all_values) , Toast.LENGTH_SHORT).show()
            } else {
                registerUser(email, password, name)
            }
        }
    }

    private fun registerUser(email: String, password: String, name: String ) {
        FavoriteDAO().insertUser(email , password , name , this@RegisterActivity)
        goToLogin()
    }

    private fun goToLogin() {
        startActivity(Intent(this , LoginActivity::class.java))
    }
}