package com.konaet.cover.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konaet.cover.core.data.DeviceRepository
import com.konaet.cover.core.data.PoolRepository
import com.konaet.cover.core.model.ProtectedDevice
import com.konaet.cover.core.model.Pool
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val devices: List<ProtectedDevice>,
        val pools: List<Pool>
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val poolRepository: PoolRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val devices = deviceRepository.getDevices()
                val pools = poolRepository.getPools()
                _uiState.value = HomeUiState.Success(devices, pools)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Failed to load data")
            }
        }
    }
}
