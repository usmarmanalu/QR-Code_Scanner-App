package com.dicoding.qrcodescannerapp.adapter

import android.annotation.*
import android.view.*
import androidx.recyclerview.widget.*
import coil.*
import com.dicoding.qrcodescannerapp.*
import com.dicoding.qrcodescannerapp.data.local.*
import com.dicoding.qrcodescannerapp.databinding.*

//wifi
class GenerateWifiAdapter(private val onItemClicked: (GenerateWifiEntity) -> Unit) :
    ListAdapter<GenerateWifiEntity, GenerateWifiAdapter.GenerateWifiHistoryViewHolder>(
        DiffCallback
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenerateWifiHistoryViewHolder {
        val binding =
            ItemGenerateWifiHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return GenerateWifiHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenerateWifiHistoryViewHolder, position: Int) {
        val scanHistory = getItem(position)
        holder.bind(scanHistory)
        holder.itemView.setOnClickListener {
            onItemClicked(scanHistory)
        }
    }

    inner class GenerateWifiHistoryViewHolder(private val binding: ItemGenerateWifiHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(generateWifiEntity: GenerateWifiEntity) {
            binding.tvWifi.text = generateWifiEntity.generateNameWifi
            binding.tvWifiPass.text = "Pass: " + " " + generateWifiEntity.generatePasswordWifi

            when (generateWifiEntity.type) {
                HistoryType.WIFI -> binding.ivType.setImageResource(R.drawable.wifi)

                else -> binding.ivType.setImageResource(R.drawable.logo)
            }

            if (generateWifiEntity.imageUri != null) {
                binding.imageViewQrCode.load(
                    generateWifiEntity.imageUri
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
        private val DiffCallback = object : DiffUtil.ItemCallback<GenerateWifiEntity>() {
            override fun areItemsTheSame(
                oldItem: GenerateWifiEntity,
                newItem: GenerateWifiEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: GenerateWifiEntity,
                newItem: GenerateWifiEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}