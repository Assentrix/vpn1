package com.example.vpnapp.utils.extensions

import android.app.Activity
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.vpnapp.R
import com.example.vpnapp.databinding.DialogLogoutWarningBinding
import jp.wasabeef.blurry.Blurry

fun Activity.showCustomDialog(
    deviceName: String,
    onSuccess: () -> Unit,
    onFailure: () -> Unit
) {
    // Inflate the custom dialog layout using ViewBinding
    val dialogBinding = DialogLogoutWarningBinding.inflate(LayoutInflater.from(this))
    // Create the dialog
    val dialog = AlertDialog.Builder(this)
        .setView(dialogBinding.root)
        .create()

    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    // Set horizontal margins for the dialog after it's shown
    dialog.window?.let { window ->
        val layoutParams = window.attributes
        layoutParams.width =
            ViewGroup.LayoutParams.MATCH_PARENT // Ensures dialog width spans the screen
        window.attributes = layoutParams

        val contentView = window.findViewById<ViewGroup>(android.R.id.content)
        val params = contentView.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(50, 0, 50, 0) // Set left and right margins to 50px
        contentView.layoutParams = params
    }

    // Apply background blur if Android 12+ (API 31)
    // TODO: Check for alternatives so will run on almost all devices
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val rootView = window.decorView
        rootView.setRenderEffect(
            RenderEffect.createBlurEffect(
                50f,
                50f,
                Shader.TileMode.REPEAT
            )
        )

        dialog.setOnDismissListener {
            rootView.setRenderEffect(null)
        }
    } else {
        val contentView = findViewById<ViewGroup>(android.R.id.content)
        // Apply Blurry to the contentView directly for older Android versions
        Blurry.with(this)
            .radius(10) // Radius of blur
            .sampling(2) // Sampling factor for performance
            .onto(contentView) // Apply blur effect to the contentView

        dialog.setOnDismissListener {
            // This clears the blur effect when the dialog is dismissed
            Blurry.delete(contentView)
        }
    }
    dialogBinding.apply {
        tvWarningTitle.text = getString(R.string.logout_warning, deviceName)

        btnLogout.setOnClickListener {
            // TODO: Perform some logic

            // Then Perform on success block if success or perform the failure block.
            onSuccess.invoke()

            // Close the dialog
            dialog.dismiss()
        }

        btnBack.setOnClickListener {
            // TODO:
            dialog.dismiss()
        }
    }

    // Show the dialog
    dialog.show()
}