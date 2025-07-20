package com.example.vpnapp.ui.tooManyDevice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vpnapp.databinding.ItemTooManyDeviceBinding
import com.example.vpnapp.ui.tooManyDevice.model.LoggedDeviceInfo

class DeviceListAdapter(private val items: MutableList<LoggedDeviceInfo>, private val onDeviceLogoutClick: (Int) -> Unit) :
    RecyclerView.Adapter<DeviceListAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(private val binding: ItemTooManyDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnLogoutFromThis.setOnClickListener {
                onDeviceLogoutClick(adapterPosition)
            }
        }

        fun bind(item: LoggedDeviceInfo) {
            binding.apply {
                txtDeviceName.text = item.deviceName
                txtCreatedDate.text = item.deviceCreatedDate
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemTooManyDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}