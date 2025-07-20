package com.example.vpnapp.ui.auth.viewModel

import android.content.Context
import android.text.SpannableString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vpnapp.ui.auth.repository.AccountNumberCreatedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountNumberCreatedViewModel @Inject constructor(
    private val repository: AccountNumberCreatedRepository
) : ViewModel() {

    private val _animatedText = MutableLiveData<String>()
    val animatedText: LiveData<String> get() = _animatedText

    private val _deviceNameWithStyle = MutableLiveData<SpannableString>()
    val deviceNameWithStyle: LiveData<SpannableString> get() = _deviceNameWithStyle

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun appendDeviceNameWithStyle(context: Context, baseText: String, deviceName: String) {
        _deviceNameWithStyle.value = repository.getStyledDeviceName(context, baseText, deviceName)
    }

    fun animateTextSelection(targetText: String, delayPerStep: Long = 100L, iterations: Int = 20) {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.Main) {
            repeat(iterations) {
                _animatedText.value = repository.generateRandomAccountNumber()
                delay(delayPerStep)
            }
            _animatedText.value = targetText.chunked(4).joinToString(" ")
            _isLoading.value = false
        }
    }
}