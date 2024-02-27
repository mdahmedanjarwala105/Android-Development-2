package com.maabook.maabook.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.maabook.maabook.R

class SplashScreen : AppCompatActivity() {

    private val SPLASH_TIME: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
        }, SPLASH_TIME)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}