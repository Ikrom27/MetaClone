package ru.metaclone.authorization.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.metaclone.auth_repository.IAuthRepository
import ru.metaclone.auth_repository.model.AuthInfo
import ru.metaclone.auth_repository.model.Gender
import ru.metaclone.auth_repository.model.UserRegisterInfo

class RegisterViewModel(
    private val authRepository: IAuthRepository
) : ViewModel() {

    fun registerUser(
        firstName: String,
        lastName: String,
        gender: Gender,
        birthday: Long,
        login: String,
        password: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.registerUser(
                userInfo = UserRegisterInfo(
                    firstName = firstName,
                    lastName = lastName,
                    gender = gender,
                    birthday = birthday
                ),
                authInfo = AuthInfo(
                    login = login,
                    password = password
                )
            )
        }
    }
}