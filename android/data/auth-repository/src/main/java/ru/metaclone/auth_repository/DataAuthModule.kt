package ru.metaclone.auth_repository

import org.koin.dsl.module

val dataAuthModule = module {
    single<IAuthRepository>{
        AuthRepositoryImpl()
    }
}