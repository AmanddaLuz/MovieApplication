package com.amandaluz.movieapplication.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.amandaluz.movieapplication.R
import com.amandaluz.movieapplication.view.home.activity.HomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        lifecycleScope.launch {
            while (true) {
                delay(3000)
                start()
                finish()
            }
        }
    }

    private fun start() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

}