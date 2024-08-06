package com.dicoding.qrcodescannerapp.base

import android.os.*
import android.view.*
import androidx.fragment.app.*
import androidx.viewbinding.*

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    abstract fun getLogTag(): String
    val binding: VB by lazy {
        getViewBinding()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    abstract fun getViewBinding(): VB
}