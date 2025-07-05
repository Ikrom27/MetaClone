package ru.metaclone.network

interface TokenRepository {
    fun getAccessToken(): String
    fun getRefreshToken(): String
    fun saveAccessToken(token: String)
    fun saveRefreshToken(token: String)
}