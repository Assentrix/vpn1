package com.example.vpnapp.ui.dummy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vpnapp.ui.dummy.model.ExampleData
import com.example.vpnapp.ui.dummy.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExampleViewModel @Inject constructor(private val apiRepository: ApiRepository) : ViewModel() {

    private val _exampleData = MutableLiveData<Result<ExampleData>>()
    val exampleData: LiveData<Result<ExampleData>> get() = _exampleData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun fetchExampleData() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val result = apiRepository.getExampleData()
            _exampleData.postValue(result)
            _isLoading.postValue(false)
        }
    }
}