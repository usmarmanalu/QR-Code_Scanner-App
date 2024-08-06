package com.dicoding.qrcodescannerapp.adapter

import androidx.appcompat.app.*
import androidx.fragment.app.*
import androidx.viewpager2.adapter.*
import com.dicoding.qrcodescannerapp.fragment.history.*

class PagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HistoryScannerFragment()
            1 -> TextHistoryGenerateFragment()
            2 -> WebHistoryGenerateFragment()
            3 -> WifiHistoryGenerateFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
