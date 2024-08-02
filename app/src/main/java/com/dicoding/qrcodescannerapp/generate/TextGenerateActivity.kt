package com.dicoding.qrcodescannerapp.generate

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
import com.dicoding.qrcodescannerapp.util.ImageUtils.saveQRCodeToGallery
import dagger.hilt.android.*

@AndroidEntryPoint
class TextGenerateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTextGenerateBinding
    private var generatedQRBitmap: Bitmap? = null

    private val generateTextViewModel: GenerateTextViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTextGenerateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor =
            ContextCompat.getColor(this, R.color.orange)

        generateText()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@TextGenerateActivity))
            title = "Text Generate"
            setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this@TextGenerateActivity,
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

    private fun generateText() {
        binding.apply {
            btnGenerate.setOnClickListener {
                val inputText = editTextGenerate.text.toString()
                if (inputText.isNotEmpty()) {
                    progresBar.visibility = View.VISIBLE
                    mainIvQr.setImageBitmap(null)
                    tvTextGenerate.text = inputText

                    Handler(Looper.getMainLooper()).postDelayed({
                        val mQRBitmap = QrUtility.generateQR(inputText)

                        if (mQRBitmap != null) {
                            runOnUiThread {
                                editTextGenerate.text!!.clear()
                                mainIvQr.setImageBitmap(mQRBitmap)
                                tvQrCodeLabel.visibility = View.VISIBLE
                                btnDwonload.visibility = View.VISIBLE
                                tvTextGenerate.visibility = View.VISIBLE
                                Toast.makeText(
                                    this@TextGenerateActivity,
                                    getString(R.string.success_in_creating_QR_code),
                                    Toast.LENGTH_SHORT
                                ).show()
                                generatedQRBitmap = mQRBitmap

                                val imageUri =
                                    saveBitmapToFile(this@TextGenerateActivity, mQRBitmap)

                                val currentTimeMillis= System.currentTimeMillis()

                                val generateHistory = GenerateTextEntity(
                                    generate = inputText,
                                    timestamp = currentTimeMillis,
                                    imageUri = imageUri.toString(),
                                    type = HistoryType.TEXT
                                )
                                generateTextViewModel.insertGenerateHistory(generateHistory)
                            }
                        } else {
                            runOnUiThread {
                                Toast.makeText(
                                    this@TextGenerateActivity,
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
                    editTextGenerate.error = getString(R.string.text_cannot_be_empty)
                }
            }
        }
    }

    private fun showDownloadConfirmationDialog(bitmap: Bitmap) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.download_qr_code))
            .setMessage(getString(R.string.do_you_want_to_download_the_qr_code))
            .setPositiveButton(R.string.yes) { dialog, _ ->
                saveQRCodeToGallery(this, bitmap)
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
