package com.dicoding.qrcodescannerapp.generate

import android.annotation.*
import android.graphics.*
import android.graphics.drawable.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.activity.*
import androidx.appcompat.app.*
import androidx.core.content.*
import com.dicoding.qrcodescannerapp.R
import com.dicoding.qrcodescannerapp.data.local.*
import com.dicoding.qrcodescannerapp.databinding.*
import com.dicoding.qrcodescannerapp.ui.viewmodels.*
import com.dicoding.qrcodescannerapp.util.*
import com.dicoding.qrcodescannerapp.util.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.qrcodescannerapp.util.FileUtils.saveBitmapToFile
import dagger.hilt.android.*

@AndroidEntryPoint
class WifiGenerateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWifiGenerateBinding
    private var generatedQRBitmap: Bitmap? = null

    private val generateWifiViewModel: GenerateWifiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWifiGenerateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.orange)

        generateWiFi()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@WifiGenerateActivity))
            title = "WiFi Generate"
            setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this@WifiGenerateActivity,
                        R.color.orange
                    )
                )
            )
        }

        binding.btnDwonload.setOnClickListener {
            if (generatedQRBitmap != null) {
                showDownloadConfirmationDialog(generatedQRBitmap!!)
            } else {
                Toast.makeText(this, "Generate a QR code first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun generateWiFi() {
        binding.apply {
            btnGenerate.setOnClickListener {
                val inputTextWifi = editTextWifi.text.toString()
                val inputTestPassword = editTextWifiPass.text.toString()
                if (inputTextWifi.isNotEmpty() || inputTestPassword.isNotEmpty()) {
                    progresBar.visibility = View.VISIBLE
                    mainIvQr.setImageBitmap(null)
                    tvTextGenerate.text = "WiFi : $inputTextWifi Pass : + $inputTestPassword"

                    Handler(Looper.getMainLooper()).postDelayed({
                        val mQRBitmap = QrUtility.generateWifiQR(inputTextWifi, inputTestPassword)

                        if (mQRBitmap != null) {
                            runOnUiThread {
                                editTextWifi.text!!.clear()
                                editTextWifiPass.text!!.clear()
                                mainIvQr.setImageBitmap(mQRBitmap)
                                tvQrCodeLabel.visibility = View.VISIBLE
                                btnDwonload.visibility = View.VISIBLE
                                tvTextGenerate.visibility = View.VISIBLE
                                Toast.makeText(
                                    this@WifiGenerateActivity,
                                    getString(R.string.success_in_creating_QR_code),
                                    Toast.LENGTH_SHORT
                                ).show()
                                generatedQRBitmap = mQRBitmap

                                val imageUri =
                                    saveBitmapToFile(this@WifiGenerateActivity, mQRBitmap)

                                val generateWifiHistoryEntity = GenerateWifiEntity(
                                    generateNameWifi = inputTextWifi,
                                    generatePasswordWifi = inputTestPassword,
                                    timestamp = System.currentTimeMillis(),
                                    imageUri = imageUri.toString(),
                                    type = HistoryType.WIFI
                                )

                                generateWifiViewModel.insertGenerateWifiHistory(
                                    generateWifiHistoryEntity
                                )
                            }
                        } else {
                            runOnUiThread {
                                Toast.makeText(
                                    this@WifiGenerateActivity,
                                    getString(R.string.failed_to_create_QR_code),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        runOnUiThread {
                            progresBar.visibility = View.GONE
                        }
                    }, 1000) // Simulate delay for demonstration purposes
                } else {
                    editTextWifi.error = getString(R.string.text_cannot_be_empty)
                }
            }
        }
    }

    private fun showDownloadConfirmationDialog(bitmap: Bitmap) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.download_qr_code))
            .setMessage(getString(R.string.do_you_want_to_download_the_qr_code))
            .setPositiveButton(R.string.yes) { dialog, _ ->
                ImageUtils.saveQRCodeToGallery(this, bitmap)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
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