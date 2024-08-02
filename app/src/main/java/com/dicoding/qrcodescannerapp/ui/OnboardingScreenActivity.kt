package com.dicoding.qrcodescannerapp.ui

import android.content.*
import android.os.*
import androidx.activity.*
import androidx.appcompat.app.*
import com.dicoding.qrcodescannerapp.*
import com.dicoding.qrcodescannerapp.databinding.*
import dagger.hilt.android.*

class OnboardingScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnboardingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val sharedPreferences = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(MainActivity.KEY_ONBOARDING_COMPLETED, true).apply()

        binding.btnStarted.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}