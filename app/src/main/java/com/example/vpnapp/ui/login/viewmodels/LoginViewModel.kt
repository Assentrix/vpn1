package com.example.vpnapp.ui.login.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vpnapp.ui.common.repositories.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val isUserLoggedIn = dataStoreRepository.getIsUserLoggedIn()

    fun saveIsUserLoggedIn(value: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.saveIsUserLoggedIn(value)
        }
    }
}
