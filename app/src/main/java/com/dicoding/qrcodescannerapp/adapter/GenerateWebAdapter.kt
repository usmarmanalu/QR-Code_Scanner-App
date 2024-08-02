package com.dicoding.qrcodescannerapp.adapter

import android.view.*
import androidx.recyclerview.widget.*
import coil.*
import com.dicoding.qrcodescannerapp.*
import com.dicoding.qrcodescannerapp.data.local.*
import com.dicoding.qrcodescannerapp.databinding.*

class GenerateWebAdapter(private val onItemClicked: (GenerateWebEntity) -> Unit) :
    ListAdapter<GenerateWebEntity, GenerateWebAdapter.GenerateHistoryViewHolder>(
        DiffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenerateHistoryViewHolder {
        val binding =
            ItemGenerateHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenerateHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenerateHistoryViewHolder, position: Int) {
        val scanHistory = getItem(position)
        holder.bind(scanHistory)
        holder.itemView.setOnClickListener {
            onItemClicked(scanHistory)
        }
    }

    inner class GenerateHistoryViewHolder(private val binding: ItemGenerateHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(generateWebEntity: GenerateWebEntity) {
            binding.textViewResult.text = generateWebEntity.generate

            when (generateWebEntity.type) {
                HistoryType.WEBSITE -> binding.ivType.setImageResource(R.drawable.website)

                else -> binding.ivType.setImageResource(R.drawable.logo)
            }

            if (generateWebEntity.imageUri != null) {
                binding.imageViewQrCode.load(
                    generateWebEntity.imageUri
                ) {
                    crossfade(true)
                    placeholder(R.drawable.logo)
                }
            } else {
                binding.imageViewQrCode.setImageResource(R.drawable.logo)
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<GenerateWebEntity>() {
            override fun areItemsTheSame(
                oldItem: GenerateWebEntity,
                newItem: GenerateWebEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: GenerateWebEntity,
                newItem: GenerateWebEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}