package com.example.vpnapp.ui.selectLocation.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vpnapp.R
import com.example.vpnapp.databinding.ItemCountryListBinding
import com.example.vpnapp.ui.selectLocation.model.Country

class CountryAdapter(
    private val countries: List<Country>,
    private val onFinalSelection: (Int, Int, Int) -> Unit,
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    inner class CountryViewHolder(val binding: ItemCountryListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = ItemCountryListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]
        val binding = holder.binding

        // Set country name and selection state
        binding.tvCountryTitle.text = country.name
        binding.radioCountry.isChecked = country.isSelected
        binding.radioCountry.buttonTintList = ColorStateList.valueOf(Color.WHITE)
        if (country.isExpanded) {
            binding.rootLayout.background = ContextCompat.getDrawable(binding.root.context, R.drawable.bg_select_location_expanded)
            binding.innerCellCountry.background = ContextCompat.getDrawable(binding.root.context, R.drawable.expanded_country_cell_bg)
        } else {
            binding.rootLayout.background = ContextCompat.getDrawable(binding.root.context, R.drawable.bg_select_location_normal)
            binding.innerCellCountry.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.locationCellBgNormal))
        }
        if (country.isSelected && !country.isExpanded) {
            binding.rootLayout.background = ContextCompat.getDrawable(binding.root.context, R.drawable.selected_country_bg)
            binding.innerCellCountry.background.alpha = 0
        }
        // Expand/collapse logic
        binding.cityRecyclerView.visibility = if (country.isExpanded) {
            binding.btnCountryExpand.rotation = 0f
            View.VISIBLE
        } else {
            binding.btnCountryExpand.rotation = 180f
            View.GONE
        }
        binding.btnCountryExpand.setOnClickListener {
            country.isExpanded = !country.isExpanded
            notifyItemChanged(position)
        }

        // RadioButton click logic
        binding.radioCountry.setOnClickListener {
            resetAllCountries() // Reset all other countries
            country.isSelected = true
            notifyDataSetChanged()
        }

        binding.root.setOnClickListener {
            resetAllCountries() // Reset all other countries
            country.isSelected = true
            notifyDataSetChanged()
        }

        // Nested CityAdapter
        binding.cityRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CityAdapter(country.cities, onCitySelected = { cityPosition ->
                resetAllCountries() // Reset cities and servers
                country.isSelected = true
                country.cities[cityPosition].isSelected = true
                notifyDataSetChanged()
            }) { serverIdx, cityIdx ->
                resetAllCountries()
                country.isSelected = true
                country.cities[cityIdx].isSelected = true
                country.cities[cityIdx].servers[serverIdx].isSelected = true
                onFinalSelection(position, cityIdx, serverIdx)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount() = countries.size

    // Helper to reset all countries
    private fun resetAllCountries() {
        countries.forEach { country ->
            country.isSelected = false
            resetAllCities(country)
        }
    }

    // Helper to reset cities and servers
    private fun resetAllCities(country: Country) {
        country.cities.forEach { city ->
            city.isSelected = false
            resetAllServers(country)
        }
    }

    private fun resetAllServers(country: Country) {
        country.cities.forEach {
            it.isSelected = false
            it.servers.forEach { server ->
                server.isSelected = false
            }
        }
    }
}
