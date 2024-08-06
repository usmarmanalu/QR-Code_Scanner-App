package com.dicoding.qrcodescannerapp.ui

import android.annotation.*
import android.graphics.drawable.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.*
import androidx.core.content.*
import com.dicoding.qrcodescannerapp.*
import com.dicoding.qrcodescannerapp.databinding.*
import com.dicoding.qrcodescannerapp.util.DrawableUtils.getWhiteBackArrowDrawable

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupSettingsOptions()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@SettingActivity))
            title = getString(R.string.setting)
            setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this@SettingActivity,
                        R.color.orange
                    )
                )
            )
        }
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun setupSettingsOptions() {
        val switchVibrate = binding.root.findViewById<Switch>(R.id.switch_vibrate)
        val switchBeep = binding.root.findViewById<Switch>(R.id.switch_beep)

        val tvRateUs = binding.root.findViewById<RelativeLayout>(R.id.rateUs)
        val tvPrivacyPolicy = binding.root.findViewById<RelativeLayout>(R.id.privacyPolicy)
        val tvShare = binding.root.findViewById<RelativeLayout>(R.id.share)

        // Vibrate switch
        switchVibrate?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Vibrate ON", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Vibrate OFF", Toast.LENGTH_SHORT).show()
            }
        }

        // Beep switch
        switchBeep?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Beep ON", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Beep OFF", Toast.LENGTH_SHORT).show()
            }
        }

        // Rate Us option
        tvRateUs?.setOnClickListener {
            Toast.makeText(this, "Navigating to Rate Us", Toast.LENGTH_SHORT).show()
        }

        // Privacy Policy option
        tvPrivacyPolicy?.setOnClickListener {
            Toast.makeText(this, "Navigating to Privacy Policy", Toast.LENGTH_SHORT).show()
        }

        // Share option
        tvShare?.setOnClickListener {
            Toast.makeText(this, "Sharing the app", Toast.LENGTH_SHORT).show()
        }
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()  // Handle back button press
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
