package com.konaet.cover.feature.auth

import androidx.lifecycle.ViewModel
import com.konaet.cover.core.model.AuthResponse
import com.konaet.cover.core.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

sealed class AuthUiState {
    object Idle : AuthUiState()
    data class Success(val user: AuthResponse) : AuthUiState()
}

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

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
            ),
        )
    }
}
