package com.example.vpnapp.ui.selectLocation.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vpnapp.databinding.ItemProviderBinding
import com.example.vpnapp.ui.selectLocation.model.Provider

class ProviderAdapter(private val itemList: List<Provider>) :
    RecyclerView.Adapter<ProviderAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemProviderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProviderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        binding.providerCheckbox.buttonTintList = ColorStateList.valueOf(Color.WHITE)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.providerCheckbox.text = item.text
        holder.binding.providerCheckbox.isChecked = item.isChecked

        holder.binding.providerCheckbox.setOnCheckedChangeListener { _, isChecked ->
            item.isChecked = isChecked
        }
    }

    override fun getItemCount(): Int = itemList.size

    fun getCheckedItems(): List<Provider> {
        return itemList.filter { it.isChecked }
    }
}