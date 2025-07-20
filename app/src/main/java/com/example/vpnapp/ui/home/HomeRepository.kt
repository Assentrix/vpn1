package com.example.vpnapp.ui.home

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import androidx.core.content.ContextCompat
import com.example.vpnapp.R
import javax.inject.Inject

class HomeRepository @Inject constructor() {
    fun getStyledText(
        context: Context,
        baseText: String,
        value: String
    ): SpannableString {
        val finalText = "$baseText $value"
        val spannableString = SpannableString(finalText)

        val deviceNameStart = baseText.length + 1
        val deviceNameEnd = finalText.length

        val textSubheadingColor = ContextCompat.getColor(context, R.color.textSubHeading)
        val colorSpan = ForegroundColorSpan(textSubheadingColor)
        spannableString.setSpan(
            colorSpan,
            deviceNameStart,
            deviceNameEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val typefaceSpan = TypefaceSpan("poppins_regular")
        spannableString.setSpan(
            typefaceSpan,
            deviceNameStart,
            deviceNameEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableString
    }
}