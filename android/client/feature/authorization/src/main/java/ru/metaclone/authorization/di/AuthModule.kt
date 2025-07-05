package ru.metaclone.authorization.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.metaclone.authorization.viewmodel.LoginViewModel
import ru.metaclone.authorization.viewmodel.RegisterViewModel

val featureAuthModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
}