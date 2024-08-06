package com.dicoding.qrcodescannerapp.adapter

import android.view.*
import androidx.recyclerview.widget.*
import com.dicoding.qrcodescannerapp.data.local.*
import com.dicoding.qrcodescannerapp.databinding.*
import com.dicoding.qrcodescannerapp.util.*

class ScanHistoryAdapter(private val onItemClicked: (ScanHistoryEntity) -> Unit) :
    ListAdapter<ScanHistoryEntity, ScanHistoryAdapter.ScanHistoryViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanHistoryViewHolder {
        val binding =
            ItemScanHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScanHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScanHistoryViewHolder, position: Int) {
        val scanHistory = getItem(position)
        holder.bind(scanHistory)
        holder.itemView.setOnClickListener {
            onItemClicked(scanHistory)
        }
    }

    class ScanHistoryViewHolder(private val binding: ItemScanHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(scanHistory: ScanHistoryEntity) {
            binding.textViewResult.text = scanHistory.result
            binding.textViewDate.text = formatTimestamp(scanHistory.timestamp)
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ScanHistoryEntity>() {
            override fun areItemsTheSame(
                oldItem: ScanHistoryEntity,
                newItem: ScanHistoryEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ScanHistoryEntity,
                newItem: ScanHistoryEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
