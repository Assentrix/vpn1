package com.example.vpnapp.ui.selectLocation.model

data class Country(
    val name: String,
    val cities: List<City>,
    var isExpanded: Boolean = false,
    var isSelected: Boolean = false, // Tracks selected country
    var selectedCityPosition: Int = -1 // Selected city position
)


