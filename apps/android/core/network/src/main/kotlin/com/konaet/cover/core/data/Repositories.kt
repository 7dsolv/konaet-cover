package com.konaet.cover.core.data

import com.konaet.cover.core.model.*
import com.konaet.cover.core.network.ApiService
import com.konaet.cover.core.network.AuthTokenStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenStore: AuthTokenStore,
) {
    suspend fun register(email: String, password: String): AuthResponse {
        return apiService.register(LoginRequest(email, password)).also {
            tokenStore.update(it.accessToken, it.refreshToken)
        }
    }

    suspend fun login(email: String, password: String): AuthResponse {
        return apiService.login(LoginRequest(email, password)).also {
            tokenStore.update(it.accessToken, it.refreshToken)
        }
    }

    suspend fun refresh(refreshToken: String): AccessTokenResponse {
        return apiService.refresh(mapOf("refreshToken" to refreshToken)).also {
            tokenStore.update(it.accessToken)
        }
    }
}

@Singleton
class DeviceRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getDevices(): List<ProtectedDevice> {
        return apiService.getDevices()
    }

    suspend fun getDevice(id: String): ProtectedDevice {
        return apiService.getDevice(id)
    }

    suspend fun createDevice(device: ProtectedDevice): ProtectedDevice {
        return apiService.createDevice(device)
    }

    suspend fun updateDevice(id: String, device: ProtectedDevice): ProtectedDevice {
        return apiService.updateDevice(id, device)
    }

    suspend fun deleteDevice(id: String) {
        return apiService.deleteDevice(id)
    }
}

@Singleton
class PoolRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getPools(): List<Pool> {
        return apiService.getPools()
    }

    suspend fun getPool(id: String): Pool {
        return apiService.getPool(id)
    }

    suspend fun joinPool(id: String): Coverage {
        return apiService.joinPool(id)
    }
}

@Singleton
class ClaimRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getClaims(): List<Claim> {
        return apiService.getClaims()
    }

    suspend fun getClaim(id: String): Claim {
        return apiService.getClaim(id)
    }

    suspend fun createClaim(request: CreateClaimRequest): Claim {
        return apiService.createClaim(request)
    }

    suspend fun submitClaim(id: String): Claim {
        return apiService.submitClaim(id)
    }

    suspend fun approveClaim(id: String): Claim {
        return apiService.approveClaim(id)
    }

    suspend fun rejectClaim(id: String): Claim {
        return apiService.rejectClaim(id)
    }
}
