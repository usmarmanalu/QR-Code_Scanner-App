package com.dicoding.qrcodescannerapp.adapter

import android.view.*
import androidx.recyclerview.widget.*
import coil.*
import com.dicoding.qrcodescannerapp.*
import com.dicoding.qrcodescannerapp.data.local.*
import com.dicoding.qrcodescannerapp.databinding.*

class GenerateTextAdapter(private val onItemClicked: (GenerateTextEntity) -> Unit) :
    ListAdapter<GenerateTextEntity, GenerateTextAdapter.GenerateHistoryViewHolder>(
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
        fun bind(generateTextEntity: GenerateTextEntity) {
            binding.textViewResult.text = generateTextEntity.generate

            when (generateTextEntity.type) {
                HistoryType.TEXT -> binding.ivType.setImageResource(R.drawable.text)

                else -> binding.ivType.setImageResource(R.drawable.logo)
            }

            if (generateTextEntity.imageUri != null) {
                binding.imageViewQrCode.load(
                    generateTextEntity.imageUri
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
        private val DiffCallback = object : DiffUtil.ItemCallback<GenerateTextEntity>() {
            override fun areItemsTheSame(
                oldItem: GenerateTextEntity,
                newItem: GenerateTextEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: GenerateTextEntity,
                newItem: GenerateTextEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}