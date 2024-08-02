package com.dicoding.qrcodescannerapp.permission

interface IGetPermissionListener {
    fun onPermissionGranted()
    fun onPermissionDenied()
    fun onPermissionRationale()
}