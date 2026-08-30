package com.konaet.cover.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konaet.cover.core.data.AuthRepository
import com.konaet.cover.core.model.AuthResponse
import com.konaet.cover.core.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: AuthResponse) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val result = authRepository.login(email, password)
                _uiState.value = AuthUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val result = authRepository.register(email, password)
                _uiState.value = AuthUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "Registration failed")
            }
        }
    }

    fun continueInDemoMode() {
        _uiState.value = AuthUiState.Success(
            AuthResponse(
                accessToken = "demo-access-token",
                refreshToken = "demo-refresh-token",
                expiresIn = 3600,
                user = User(
                    id = "demo-user",
                    email = "demo@konaet.local",
                    createdAt = "2026-08-30T00:00:00Z",
                ),
            )
        )
    }
}
