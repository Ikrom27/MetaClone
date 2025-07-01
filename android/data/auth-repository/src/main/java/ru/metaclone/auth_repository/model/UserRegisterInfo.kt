package ru.metaclone.auth_repository.model

data class UserRegisterInfo(
    val firstName: String,
    val lastName: String,
    val birthday: Long,
    val gender: Gender
)