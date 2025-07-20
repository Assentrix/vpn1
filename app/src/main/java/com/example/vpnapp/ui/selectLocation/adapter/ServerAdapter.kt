package com.example.vpnapp.ui.selectLocation.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vpnapp.databinding.ItemServerListBinding
import com.example.vpnapp.ui.selectLocation.model.Server

class ServerAdapter(
    private val servers: List<Server>,
    private val onServerSelected: (Int) -> Unit // Callback when a server is selected
) : RecyclerView.Adapter<ServerAdapter.ServerViewHolder>() {

    inner class ServerViewHolder(val binding: ItemServerListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServerViewHolder {
        val binding = ItemServerListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ServerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServerViewHolder, position: Int) {
        val server = servers[position]
        val binding = holder.binding

        // Set server name and selection state
        binding.tvServerName.text = server.name
        binding.radioServer.isChecked = server.isSelected
        binding.radioServer.buttonTintList = ColorStateList.valueOf(Color.WHITE)
        // RadioButton click logic
        binding.root.setOnClickListener {
            onServerSelected(position)
        }
    }

    override fun getItemCount() = servers.size
}