package ru.metaclone.authorization.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.metaclone.auth_repository.IAuthRepository
import ru.metaclone.auth_repository.model.AuthInfo

class LoginViewModel(
    val authRepository: IAuthRepository
): ViewModel() {

    fun login(
        login: String,
        password: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.login(
                AuthInfo(
                    login = login,
                    password = password
                )
            )
        }
    }
}