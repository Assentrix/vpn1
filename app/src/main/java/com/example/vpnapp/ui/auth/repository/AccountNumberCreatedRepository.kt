package com.example.vpnapp.ui.auth.repository

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import androidx.core.content.ContextCompat
import javax.inject.Inject

class AccountNumberCreatedRepository @Inject constructor() {
    fun getStyledDeviceName(
        context: Context,
        baseText: String,
        deviceName: String
    ): SpannableString {
        val finalText = "$baseText $deviceName"
        val spannableString = SpannableString(finalText)

        val deviceNameStart = baseText.length + 1
        val deviceNameEnd = finalText.length

        val whiteColor = ContextCompat.getColor(context, android.R.color.white)
        val colorSpan = ForegroundColorSpan(whiteColor)
        spannableString.setSpan(
            colorSpan,
            deviceNameStart,
            deviceNameEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val typefaceSpan = TypefaceSpan("poppins_medium")
        spannableString.setSpan(
            typefaceSpan,
            deviceNameStart,
            deviceNameEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableString
    }

    fun generateRandomAccountNumber(): String {
        val randomString = (1..16)
            .map { kotlin.random.Random.nextInt(0, 10) }
            .joinToString("")
        return randomString.chunked(4).joinToString(" ")
    }
}