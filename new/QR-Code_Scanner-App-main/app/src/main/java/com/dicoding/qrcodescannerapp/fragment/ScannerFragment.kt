package com.dicoding.qrcodescannerapp.fragment

import android.annotation.*
import android.content.*
import android.net.*
import android.os.*
import android.view.*
import android.view.animation.*
import android.widget.*
import androidx.fragment.app.*
import com.dicoding.qrcodescannerapp.R
import com.dicoding.qrcodescannerapp.base.*
import com.dicoding.qrcodescannerapp.databinding.*
import com.dicoding.qrcodescannerapp.scanner.*
import com.dicoding.qrcodescannerapp.ui.viewmodels.*
import com.google.android.material.dialog.*
import dagger.hilt.android.*
import java.util.regex.*

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ScannerFragment : BaseFragment<FragmentScannerBinding>() {

    private val qrCodeViewModel: ScannerViewModel by viewModels()

    private val vibrator: Vibrator by lazy {
        requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    private var isFlashEnabledForQrCode: Boolean = false
    private var isFlashEnabledForCamera: Boolean = false
    private var isScanningPaused: Boolean = false


    override fun getLogTag(): String = TAG

    override fun getViewBinding(): FragmentScannerBinding =
        FragmentScannerBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        startAnimation()
        setupFlashToggle()
    }

    override fun onDestroyView() {
        vibrator.cancel()
        super.onDestroyView()
    }

    /**
     *  Initialise views and and handle click listeners here
     */
    private fun initView() {
        qrCodeViewModel.startCamera(
            viewLifecycleOwner,
            requireContext(),
            binding.previewView,
            ::onResult
        )
    }

    /**
     * Success callback and error callback when barcode is successfully scanned. This method is also called while manually enter barcode
     */
    private fun onResult(state: ScannerViewState, result: String?) {
        if (isScanningPaused) return

        when (state) {
            ScannerViewState.Loading -> {
                binding.progresBar.visibility = View.VISIBLE
            }

            ScannerViewState.Success -> {
                binding.progresBar.visibility = View.GONE
                vibrateOnScan()
                result?.let {
                    isScanningPaused = true
                    showResultDialog(it) {
                        isScanningPaused = false
                    }
                    qrCodeViewModel.insertScanHistory(it)
                }
            }

            ScannerViewState.Error -> {
                binding.progresBar.visibility = View.GONE
                Toast.makeText(requireContext(), "error =${result}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showResultDialog(result: String, onDismiss:() -> Unit) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.scan_results))
            .setMessage(result)
            .setPositiveButton(getString(R.string.search_the_web)) { _, _ ->
                if (isValidUrl(result)) {
                    openInBrowser(result)
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.result_is_not_a_valid_URL),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                onDismiss()
            }
            .setNegativeButton(getString(R.string.share)) { _, _ ->
                shareResult(result)
                onDismiss()
            }
            .setOnDismissListener { onDismiss() }
            .show()
    }

    private fun isValidUrl(url: String): Boolean {
        val urlPattern = getString(R.string.http_https)
        val pattern = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE)
        return pattern.matcher(url).matches()
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openInBrowser(url: String) {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(browserIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                getString(R.string.no_browsers_found), Toast.LENGTH_SHORT
            )
                .show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                requireContext(),
                getString(R.string.An_error_occurred_while_opening_the_browser),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun shareResult(result: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, result)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_scan_results)))
    }

    /**
     *  Animation for the red bar.
     */
    private fun startAnimation() {
        val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.barcode_animator)
        binding.llAnimation.startAnimation(animation)
    }

    /**
     *  Vibration mobile on Scan successful.
     */
    private fun vibrateOnScan() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        VIBRATE_DURATION,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(VIBRATE_DURATION)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Setup flash toggle buttons for QR code and camera.
     */
    private fun setupFlashToggle() {
        binding.btnFlashToggleQrCode.setOnClickListener {
            isFlashEnabledForQrCode = !isFlashEnabledForQrCode
            qrCodeViewModel.enableFlashForQrCode(isFlashEnabledForQrCode)
            updateFlashToggleIconQr(isFlashEnabledForQrCode, binding.btnFlashToggleQrCode)
        }

        binding.btnFlashToggleCamera.setOnClickListener {
            isFlashEnabledForCamera = !isFlashEnabledForCamera
            qrCodeViewModel.enableFlashForCamera(isFlashEnabledForCamera)
            updateFlashToggleIconCam(isFlashEnabledForCamera, binding.btnFlashToggleCamera)
        }
    }

    /**
     * Update flash toggle button icon based on flash status.
     */
    private fun updateFlashToggleIconQr(isEnabled: Boolean, button: ImageButton) {
        val iconResId = if (isEnabled) R.drawable.flash_on_qr else R.drawable.flash_off_qr
        button.setImageResource(iconResId)
    }

    private fun updateFlashToggleIconCam(isEnabled: Boolean, button: ImageButton) {
        val iconResId = if (isEnabled) R.drawable.flash_on_camera else R.drawable.flash_off_camera
        button.setImageResource(iconResId)
    }

    companion object {
        private const val TAG = "QrCodeReaderFragment"
        private const val VIBRATE_DURATION = 200L
    }
}