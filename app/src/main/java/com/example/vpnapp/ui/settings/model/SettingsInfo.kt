package com.example.vpnapp.ui.settings.model

data class SettingsInfo(
    val title: String,
    val topMargin: Int = 0,
    val bottomMargin: Int = 0,
    val hasInfoIcon: Boolean = false,
    val destinationId: Int = -1
)