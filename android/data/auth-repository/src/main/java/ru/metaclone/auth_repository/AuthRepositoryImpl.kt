package ru.metaclone.auth_repository

import android.util.Log
import ru.metaclone.auth_repository.model.AuthInfo
import ru.metaclone.auth_repository.model.UserRegisterInfo

class AuthRepositoryImpl : IAuthRepository {
    override suspend fun registerUser(
        userInfo: UserRegisterInfo,
        authInfo: AuthInfo
    ) {
        Log.d(TAG, "register")
    }

    override suspend fun login(authInfo: AuthInfo) {
        Log.d(TAG, "login")
    }

    companion object {
        private val TAG = AuthRepositoryImpl::class.simpleName
    }
}