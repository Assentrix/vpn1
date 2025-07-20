package com.example.vpnapp.ui.selectLocation.model

data class Server(
    val name: String,
    var isSelected: Boolean = false // Tracks selected server
)