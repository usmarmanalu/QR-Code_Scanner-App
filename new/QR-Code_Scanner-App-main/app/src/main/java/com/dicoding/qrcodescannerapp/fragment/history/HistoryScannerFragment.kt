package com.dicoding.qrcodescannerapp.fragment.history

import android.content.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.*
import androidx.fragment.app.*
import androidx.recyclerview.widget.*
import com.dicoding.qrcodescannerapp.R
import com.dicoding.qrcodescannerapp.adapter.*
import com.dicoding.qrcodescannerapp.databinding.*
import com.dicoding.qrcodescannerapp.ui.viewmodels.*
import com.dicoding.qrcodescannerapp.util.*
import com.google.android.material.snackbar.*
import dagger.hilt.android.*

@Suppress("DEPRECATION")
@AndroidEntryPoint
class HistoryScannerFragment : Fragment() {

    private var _binding: FragmentHistoryScannerBinding? = null
    private val binding get() = _binding!!

    private val scannerViewModel: ScannerViewModel by viewModels()
    private lateinit var scanHistoryAdapter: ScanHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeScanHistories()

    }

    private fun setupRecyclerView() {
        scanHistoryAdapter = ScanHistoryAdapter { scanHistory ->
            showCopyDialog(
                requireContext(),
                scanHistory.result,
                formatTimestamp(scanHistory.timestamp)
            )
        }

        with(binding.recyclerView) {
            setHasFixedSize(true)
            adapter = scanHistoryAdapter
        }

        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val scanHistory = scanHistoryAdapter.currentList[position]
                    scannerViewModel.deleteScanHistory(scanHistory)
                    Snackbar.make(binding.root, "Item deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            scannerViewModel.insertScanHistoryUndo(scanHistory)
                        }.show()
                }
            }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.recyclerView)
    }

    private fun observeScanHistories() {
        scannerViewModel.scanHistories.observe(viewLifecycleOwner) { scanHistories ->
            binding.progressBar.visibility = View.GONE
            if (scanHistories.isEmpty()) {
                binding.tvEmptyHistory.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else{
                scanHistoryAdapter.submitList(scanHistories)
                binding.tvEmptyHistory.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun showCopyDialog(context: Context, text: String?, date: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_copy, null)

        val dialogTextView = dialogView.findViewById<TextView>(R.id.dialogTextView)
        val dialogTextViewDate = dialogView.findViewById<TextView>(R.id.dialogDate)
        val btnCopy = dialogView.findViewById<Button>(R.id.btnCopy)

        dialogTextView.text = text
        dialogTextViewDate.text = date

        btnCopy.setOnClickListener {
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("QR Code Result", text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .show()
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun refreshData() {
        observeScanHistories()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
