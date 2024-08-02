package com.dicoding.qrcodescannerapp.base

import android.os.*
import androidx.appcompat.app.*
import androidx.viewbinding.*

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(){
    val binding: VB by lazy {
        getViewBinding()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
    abstract fun getLogTag(): String
    abstract fun getViewBinding(): VB
}