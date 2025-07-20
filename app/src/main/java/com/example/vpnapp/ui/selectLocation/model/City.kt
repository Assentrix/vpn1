package com.example.vpnapp.ui.selectLocation.model

data class City(
    val name: String,
    val servers: List<Server>,
    var isExpanded: Boolean = false,
    var isSelected: Boolean = false, // Tracks selected city
    var selectedServerPosition: Int = -1 // Selected server position
)
