package com.konaet.cover.core.network

import com.konaet.cover.core.model.*
import retrofit2.http.*

interface ApiService {
    // Auth endpoints
    @POST("v1/auth/register")
    suspend fun register(@Body request: LoginRequest): AuthResponse

    @POST("v1/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("v1/auth/refresh")
    suspend fun refresh(@Body body: Map<String, String>): AccessTokenResponse

    // Device endpoints
    @GET("v1/devices")
    suspend fun getDevices(): List<ProtectedDevice>

    @GET("v1/devices/{id}")
    suspend fun getDevice(@Path("id") id: String): ProtectedDevice

    @POST("v1/devices")
    suspend fun createDevice(@Body device: ProtectedDevice): ProtectedDevice

    @PATCH("v1/devices/{id}")
    suspend fun updateDevice(@Path("id") id: String, @Body device: ProtectedDevice): ProtectedDevice

    @DELETE("v1/devices/{id}")
    suspend fun deleteDevice(@Path("id") id: String): Unit

    // Pool endpoints
    @GET("v1/pools")
    suspend fun getPools(): List<Pool>

    @GET("v1/pools/{id}")
    suspend fun getPool(@Path("id") id: String): Pool

    @POST("v1/pools/{id}/join")
    suspend fun joinPool(@Path("id") id: String): Coverage

    // Claim endpoints
    @GET("v1/claims")
    suspend fun getClaims(): List<Claim>

    @GET("v1/claims/{id}")
    suspend fun getClaim(@Path("id") id: String): Claim

    @POST("v1/claims")
    suspend fun createClaim(@Body request: CreateClaimRequest): Claim

    @POST("v1/claims/{id}/submit")
    suspend fun submitClaim(@Path("id") id: String): Claim

    @POST("v1/claims/{id}/approve")
    suspend fun approveClaim(@Path("id") id: String): Claim

    @POST("v1/claims/{id}/reject")
    suspend fun rejectClaim(@Path("id") id: String): Claim
}
