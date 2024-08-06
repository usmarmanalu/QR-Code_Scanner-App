package com.dicoding.qrcodescannerapp

import android.content.*
import android.os.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.*
import androidx.activity.result.contract.*
import androidx.appcompat.app.*
import androidx.navigation.*
import androidx.navigation.ui.*
import com.dicoding.qrcodescannerapp.databinding.*
import com.dicoding.qrcodescannerapp.ui.*
import dagger.hilt.android.*

@Suppress("NAME_SHADOWING", "DEPRECATION")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false
    private lateinit var binding: ActivityMainBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val PREFS_NAME = "MainGeneratePrefs"
        const val KEY_ONBOARDING_COMPLETED = "onboardingCompleted"
        private const val KEY_PERMISSION_GRANTED = "permissionGranted"
        private const val KEY_PERMISSION_REQUESTED = "permissionRequested"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    sharedPreferences.edit().putBoolean(KEY_PERMISSION_GRANTED, true).apply()
                } else {
                    if (!sharedPreferences.getBoolean(KEY_PERMISSION_REQUESTED, false)) {
                        Toast.makeText(
                            this,
                            "Permission denied to write external storage",
                            Toast.LENGTH_SHORT
                        ).show()
                        sharedPreferences.edit().putBoolean(KEY_PERMISSION_REQUESTED, true).apply()
                    }
                }
            }

        checkStoragePermission()

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.navView.setupWithNavController(navController)

        supportActionBar?.hide()

        binding.fab.setOnClickListener {
            startActivity(Intent(this, ScannerActivity::class.java))
        }

        binding.fabHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        binding.navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_generate -> {
                    navController.navigate(R.id.navigation_generate)
                    binding.topAppBar.title = "Generator QR"
                    true
                }

                R.id.navigation_explore -> {
                    navController.navigate(R.id.navigation_explore)
                    binding.topAppBar.title = "Explore"
                    true
                }

                else -> false
            }
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuSetting -> {
                    startActivity(Intent(this, SettingActivity::class.java))
                    true
                }

                else -> false
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                if (navController.currentDestination?.id == R.id.navigation_generate ||
                    navController.currentDestination?.id == R.id.navigation_explore
                ) {
                    if (doubleBackToExitPressedOnce) {
                        finishAffinity()
                    } else {
                        doubleBackToExitPressedOnce = true
                        Toast.makeText(this@MainActivity, "Press back again to exit", Toast.LENGTH_SHORT).show()
                        Handler(Looper.getMainLooper()).postDelayed({
                            doubleBackToExitPressedOnce = false
                        }, 2000)
                    }
                } else {
                    // Allow system back button to work as usual for other destinations
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun checkStoragePermission() {
        if (!sharedPreferences.getBoolean(KEY_PERMISSION_GRANTED, false)) {
            requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
