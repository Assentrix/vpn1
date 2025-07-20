package com.example.vpnapp.ui.tooManyDevice.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vpnapp.ui.dummy.repository.ApiRepository
import com.example.vpnapp.ui.tooManyDevice.model.LoggedDeviceInfo
import com.example.vpnapp.ui.tooManyDevice.repository.TooManyDeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TooManyDeviceViewModel @Inject constructor(private val tooManyDeviceRepository: TooManyDeviceRepository) : ViewModel() {

    private val _loggedDevices = MutableLiveData<Result<MutableList<LoggedDeviceInfo>>>()
    val loggedDevices: LiveData<Result<MutableList<LoggedDeviceInfo>>> get() = _loggedDevices

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun fetchLoggedDevices() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val result = tooManyDeviceRepository.getLoggedDevices()
            _loggedDevices.postValue(result)
            _isLoading.postValue(false)
        }
    }
}