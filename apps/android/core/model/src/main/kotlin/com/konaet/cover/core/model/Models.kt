package com.konaet.cover.core.model

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class User(
    val id: String,
    val email: String,
    val status: String = "active",
    val locale: String = "pt-BR",
    val createdAt: String
)

@Serializable
data class ProtectedDevice(
    val id: String,
    val nickname: String,
    val make: String,
    val model: String,
    val purchaseValueMinor: Int? = null,
    val currency: String = "BRL",
    val status: String = "active",
    val createdAt: String
)

@Serializable
data class Pool(
    val id: String,
    val code: String,
    val name: String,
    val mode: String = "DEMO",
    val capacityMinor: Long = 1000000000,
    val status: String = "active",
    val createdAt: String
)

@Serializable
data class Coverage(
    val id: String,
    val deviceId: String,
    val poolId: String,
    val startsAt: String,
    val endsAt: String? = null,
    val status: String = "active",
    val createdAt: String
)

@Serializable
data class Claim(
    val id: String,
    val deviceId: String,
    val type: String = "LOSS",
    val state: String = "DRAFT",
    val occurredAt: String,
    val submittedAt: String? = null,
    val createdAt: String
)

@Serializable
data class Evidence(
    val id: String,
    val claimId: String,
    val objectKey: String,
    val mime: String,
    val size: Int,
    val sha3_512: String,
    val status: String = "uploaded",
    val createdAt: String
)

@Serializable
data class CausalEvent(
    val id: String,
    val eventType: String,
    val subjectType: String,
    val subjectId: String,
    val logicalClock: Long,
    val payloadSha3_512: String,
    val payloadKeccak256: String,
    val actorRef: String,
    val createdAt: String
)

@Serializable
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val user: User
)

@Serializable
data class AccessTokenResponse(
    val accessToken: String,
    val expiresIn: Long,
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class CreateClaimRequest(
    val deviceId: String,
    val type: String = "LOSS",
    val occurredAt: String,
    val description: String? = null
)

@Serializable
data class CreateEvidenceRequest(
    val claimId: String,
    val mime: String,
    val sha3_512: String
)
