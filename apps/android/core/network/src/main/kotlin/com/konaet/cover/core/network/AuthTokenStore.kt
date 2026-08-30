package com.konaet.cover.core.network

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenStore @Inject constructor() {
    @Volatile
    var accessToken: String? = null
        private set

    @Volatile
    var refreshToken: String? = null
        private set

    fun update(accessToken: String, refreshToken: String? = null) {
        this.accessToken = accessToken
        if (refreshToken != null) {
            this.refreshToken = refreshToken
        }
    }

    fun clear() {
        accessToken = null
        refreshToken = null
    }
}
