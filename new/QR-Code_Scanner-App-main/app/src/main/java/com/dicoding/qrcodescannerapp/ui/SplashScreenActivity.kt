package com.dicoding.qrcodescannerapp.ui

import android.annotation.*
import android.content.*
import android.os.*
import android.view.*
import androidx.activity.*
import androidx.appcompat.app.*
import com.dicoding.qrcodescannerapp.*
import com.dicoding.qrcodescannerapp.databinding.*

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        val sharedPreferences = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE)
        val onboardingCompleted = sharedPreferences.getBoolean(MainActivity.KEY_ONBOARDING_COMPLETED, false)

        Handler(Looper.getMainLooper()).postDelayed({
            if (onboardingCompleted) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, OnboardingScreenActivity::class.java))
            }
            finish()
        }, 1000)
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}