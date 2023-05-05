package com.amandaluz.movieapplication.view.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amandaluz.movieapplication.databinding.ActivityRegisterBinding
import com.amandaluz.movieapplication.view.home.activity.HomeActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var name : EditText
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var button : Button

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

        binding.btnConfirm.setOnClickListener {
            val name = binding.fullNameEdit.text.toString()
            val email = binding.emailEdit.text.toString()
            val password = binding.passwordEdit.text.toString()
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this , "Preencha todos os campos" , Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this , LoginActivity::class.java))
            }
        }
    }

    private fun initView() = with(binding) {
        name = fullNameEdit
        email = emailEdit
        password = passwordEdit
    }
}