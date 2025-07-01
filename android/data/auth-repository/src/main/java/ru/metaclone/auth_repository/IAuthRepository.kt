package ru.metaclone.auth_repository

import ru.metaclone.auth_repository.model.AuthInfo
import ru.metaclone.auth_repository.model.UserRegisterInfo

interface IAuthRepository {
    suspend fun registerUser(userInfo: UserRegisterInfo, authInfo: AuthInfo)
    suspend fun login(authInfo: AuthInfo)
}