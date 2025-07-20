package com.example.vpnapp.utils.extensions

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import android.os.Bundle
import androidx.annotation.AnimRes
import com.example.vpnapp.R

// Extension function for navigating with animations
fun NavController.navigateWithAnimation(
    resId: Int,
    args: Bundle? = null,
    @AnimRes enterAnim: Int = R.anim.enter_anim,
    @AnimRes exitAnim: Int = R.anim.exit_anim,
    @AnimRes popEnterAnim: Int = R.anim.pop_enter_anim,
    @AnimRes popExitAnim: Int = R.anim.pop_exit_anim
) {
    val navOptions = NavOptions.Builder()
        .setEnterAnim(enterAnim)
        .setExitAnim(exitAnim)
        .setPopEnterAnim(popEnterAnim)
        .setPopExitAnim(popExitAnim)
        .build()

    navigate(resId, args, navOptions)
}