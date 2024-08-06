package com.dicoding.qrcodescannerapp.ui

import android.os.*
import android.view.*
import androidx.activity.*
import androidx.annotation.*
import androidx.appcompat.app.*
import androidx.appcompat.widget.*
import androidx.viewpager2.widget.*
import com.dicoding.qrcodescannerapp.R
import com.dicoding.qrcodescannerapp.adapter.*
import com.dicoding.qrcodescannerapp.databinding.*
import com.dicoding.qrcodescannerapp.util.DrawableUtils.getWhiteBackArrowDrawable
import com.google.android.material.tabs.*
import dagger.hilt.android.*

@AndroidEntryPoint
class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeAsUpIndicator(getWhiteBackArrowDrawable(this))
        supportActionBar?.title = ""

        val sectionsPagerAdapter = PagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
            R.string.tab_text_3,
            R.string.tab_text_4,

        )
    }
}