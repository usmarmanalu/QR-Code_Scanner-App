package com.dicoding.qrcodescannerapp.fragment.history

import android.content.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.*
import androidx.core.graphics.drawable.*
import androidx.fragment.app.*
import androidx.recyclerview.widget.*
import coil.*
import com.dicoding.qrcodescannerapp.R
import com.dicoding.qrcodescannerapp.adapter.*
import com.dicoding.qrcodescannerapp.data.local.*
import com.dicoding.qrcodescannerapp.databinding.*
import com.dicoding.qrcodescannerapp.ui.viewmodels.*
import com.dicoding.qrcodescannerapp.util.*
import com.dicoding.qrcodescannerapp.util.ImageUtils.saveQRCodeToGallery
import com.google.android.material.snackbar.*
import dagger.hilt.android.*

@AndroidEntryPoint
class WebHistoryGenerateFragment : Fragment() {

    private var _binding: FragmentWebHistoryGenerateBinding? = null
    private val binding get() = _binding!!

    private val generateWebViewModel: GenerateWebViewModel by viewModels()
    private lateinit var generateWebAdapter: GenerateWebAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebHistoryGenerateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeGenerateHistories()
    }

    private fun setupRecyclerView() {
        generateWebAdapter = GenerateWebAdapter { generateHistory ->
            showImageDialog(
                generateHistory.imageUri,
                generateHistory.generate,
                formatTimestamp(generateHistory.timestamp)
            )
        }

        with(binding.recyclerView) {
            setHasFixedSize(true)
            adapter = generateWebAdapter
        }

        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapterPosition = viewHolder.bindingAdapterPosition
                    val generateHistory = generateWebAdapter.currentList[adapterPosition]
                    generateWebViewModel.deleteGenerateHistory(generateHistory)
                    showUndoSnackbar(generateHistory)
                }
            }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.recyclerView)
    }

    private fun showUndoSnackbar(generateWebEntity: GenerateWebEntity) {
        Snackbar.make(binding.root, "Item deleted", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                generateWebViewModel.insertGenerateHistory(generateWebEntity)
            }.show()
    }

    private fun showImageDialog(imageUri: String?, generateText: String?, generateDate: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_image, null)

        val imageViewQr = dialogView.findViewById<ImageView>(R.id.dialogImageView)
        val textView = dialogView.findViewById<TextView>(R.id.dialogTextView)
        val textViewDate = dialogView.findViewById<TextView>(R.id.dialogTextViewDate)
        val imageViewCopy = dialogView.findViewById<ImageView>(R.id.btnCopy)
        val imageViewDownload = dialogView.findViewById<ImageView>(R.id.btnDownload)

        imageUri?.let {
            imageViewQr.load(it)
        }

        textView.text = generateText ?: "No text available"
        textViewDate.text = generateDate

        imageViewCopy.setOnClickListener {
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("QR Code Result", generateText)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        imageViewDownload.setOnClickListener {
            imageViewQr.drawable?.toBitmap()?.let { bitmap ->
                saveQRCodeToGallery(requireContext(), bitmap)
            } ?: run {
                Toast.makeText(context, "Failed to download image", Toast.LENGTH_SHORT).show()
            }
        }

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .show()
    }

    private fun observeGenerateHistories() {
        generateWebViewModel.generateHistories.observe(viewLifecycleOwner) { generateHistories ->
            binding.progressBar.visibility = View.GONE
            if (generateHistories.isEmpty()) {
                binding.tvEmptyHistory.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                generateWebAdapter.submitList(generateHistories)
                binding.tvEmptyHistory.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun refreshData() {
        observeGenerateHistories()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}