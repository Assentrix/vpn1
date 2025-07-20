package com.example.vpnapp.ui.selectLocation.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vpnapp.databinding.ItemCityListBinding
import com.example.vpnapp.ui.selectLocation.model.City

class CityAdapter(
    private val cities: List<City>,
    private val onCitySelected: (Int) -> Unit, // Callback when a city is selected
    private val onServerSelected: (Int, Int) -> Unit // Callback when a server is selected
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    inner class CityViewHolder(val binding: ItemCityListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val binding = ItemCityListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cities[position]
        val binding = holder.binding

        // Set city name and selection state
        binding.tvCityTitle.text = city.name
        binding.radioCity.isChecked = city.isSelected
        binding.radioCity.buttonTintList = ColorStateList.valueOf(Color.WHITE)
        // Expand/collapse logic
        binding.rvServerList.visibility = if (city.isExpanded) View.VISIBLE else View.GONE
        binding.btnCityExpand.rotation = if (city.isExpanded) {
            0f
        } else {
            180f
        }

        binding.btnCityExpand.setOnClickListener {
            city.isExpanded = !city.isExpanded
            notifyItemChanged(position)
        }

        // RadioButton click logic
        binding.root.setOnClickListener {
            onCitySelected(position)
        }

        // Nested ServerAdapter for servers
        binding.rvServerList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ServerAdapter(city.servers) { serverPosition ->
                onServerSelected(serverPosition, position)
            }
        }
    }

    override fun getItemCount() = cities.size
}