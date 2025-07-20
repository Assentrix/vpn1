package com.example.vpnapp.ui.home.viewModel

import android.content.Context
import android.text.SpannableString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vpnapp.ui.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    private val _deviceNameWithStyle = MutableLiveData<SpannableString>()
    val deviceNameWithStyle: LiveData<SpannableString> get() = _deviceNameWithStyle

    private val _timeLeftWithStyle = MutableLiveData<SpannableString>()
    val timeLeftWithStyle: LiveData<SpannableString> get() = _timeLeftWithStyle

    private val _inIPWithStyle = MutableLiveData<SpannableString>()
    val inIPWithStyle: LiveData<SpannableString> get() = _inIPWithStyle

    private val _outIPWithStyle = MutableLiveData<SpannableString>()
    val outIPWithStyle: LiveData<SpannableString> get() = _outIPWithStyle

    private val _isVPNConnected = MutableLiveData(false)
    val isVPNConnected: LiveData<Boolean> get() = _isVPNConnected

    fun toggleVPNConnection() {
        _isVPNConnected.value = !(_isVPNConnected.value ?: false)
    }

    fun appendDeviceNameWithStyle(context: Context, baseText: String, deviceName: String) {
        _deviceNameWithStyle.value = repository.getStyledText(context, baseText, deviceName)
    }

    fun appendTimeLeftWithStyle(context: Context, baseText: String, timeLeft: String) {
        _timeLeftWithStyle.value = repository.getStyledText(context, baseText, timeLeft)
    }

    fun appendInIPAddressWithStyle(context: Context, baseText: String, inIP: String) {
        _inIPWithStyle.value = repository.getStyledText(context, baseText, inIP)
    }

    fun appendOutIPAddressWithStyle(context: Context, baseText: String, outIP: String) {
        _outIPWithStyle.value = repository.getStyledText(context, baseText, outIP)
    }
}