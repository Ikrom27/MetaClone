package ru.metaclone.authorization.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.compose.koinViewModel
import ru.metaclone.auth_repository.model.Gender
import ru.metaclone.authorization.viewmodel.RegisterViewModel
import ru.metaclone.theme.AppDimens
import ru.metaclone.theme.AppIcons
import ru.metaclone.theme.AppStrings
import ru.metaclone.theme.statusBarHeightDp
import ru.metaclone.theme.uiComponents.SelectorOption
import ru.metaclone.theme.uiComponents.UiDateField
import ru.metaclone.theme.uiComponents.UiSelector

@Composable
fun RegistrationScreen(
    viewModel: RegisterViewModel = koinViewModel(),
    defaultLoginValue: String = "",
    onBackClick: () -> Unit
) {
    UiRegistrationScreen(
        defaultLoginValue = defaultLoginValue,
        viewModel = viewModel,
        onBackClick = onBackClick
    )
}

@Composable
private fun UiRegistrationScreen(
    defaultLoginValue: String,
    viewModel: RegisterViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    val isNotSamePasswordsText = stringResource(AppStrings.passwordsIsNotSame)
    val incorrectFieldsText = stringResource(AppStrings.incorrectFields)

    val modifier = Modifier
        .fillMaxWidth()
        .padding(top = 24.dp)

    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var login by rememberSaveable { mutableStateOf(defaultLoginValue) }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordError by rememberSaveable { mutableStateOf("") }
    var birthdayMillis by rememberSaveable { mutableLongStateOf(System.currentTimeMillis()) }
    var gender by rememberSaveable { mutableStateOf<Gender?>(null) }

    val isValidFields = firstName.isNotBlank() && lastName.isNotBlank() && login.isNotBlank() &&
            password.isNotBlank() && gender != null && passwordError.isEmpty()

    fun validatePasswords() {
        passwordError =
            if (confirmPassword.isNotBlank() && confirmPassword != password)
                isNotSamePasswordsText
            else
                ""
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp + statusBarHeightDp())
                    .size(24.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable(onClick = onBackClick)
            ) {
                Icon(
                    painter = painterResource(AppIcons.chevronLeft),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .padding(horizontal = AppDimens.MAIN_HORIZONTAL_PADDING)
        ) {
            item {
                Text(
                    text = stringResource(AppStrings.register),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                )

                UiAuthFormTextField(
                    modifier = modifier,
                    label = stringResource(AppStrings.firstName),
                    value = firstName,
                    onValueChanged = { firstName = it }
                )

                UiAuthFormTextField(
                    modifier = modifier,
                    label = stringResource(AppStrings.lastName),
                    value = lastName,
                    onValueChanged = { lastName = it }
                )

                UiSelector(
                    modifier = modifier,
                    label = stringResource(AppStrings.gender),
                    selectedOption = gender?.let {
                        SelectorOption(it.value, it)
                    },
                    onOptionSelected = { option ->
                        gender = option.value as Gender
                    },
                    options = listOf(
                        SelectorOption(Gender.MALE.value, Gender.MALE),
                        SelectorOption(Gender.FEMALE.value, Gender.FEMALE)
                    )
                )

                UiDateField(
                    modifier = modifier,
                    selectedDateMillis = birthdayMillis,
                    onDateSelected = { birthdayMillis = it }
                )

                UiAuthFormTextField(
                    modifier = modifier,
                    label = stringResource(AppStrings.login),
                    value = login,
                    onValueChanged = { login = it }
                )

                UiAuthFormPasswordField(
                    modifier = modifier,
                    value = password,
                    onValueChanged = {
                        password = it
                        validatePasswords()
                    }
                )

                UiAuthFormPasswordField(
                    modifier = modifier,
                    label = stringResource(AppStrings.confirmPassword),
                    value = confirmPassword,
                    onValueChanged = {
                        confirmPassword = it
                        validatePasswords()
                    },
                    isError = passwordError.isNotBlank(),
                    errorText = passwordError
                )

                UiAuthButton(
                    modifier = modifier,
                    text = stringResource(AppStrings.register),
                    onClick = {
                        if (isValidFields)
                            viewModel.registerUser(
                                firstName = firstName,
                                lastName = lastName,
                                gender = gender!!,
                                birthday = birthdayMillis,
                                login = login,
                                password = password
                            )
                        else
                            Toast.makeText(context, incorrectFieldsText, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun RegistrationPreview() {
    UiRegistrationScreen(
        defaultLoginValue = "",
        viewModel = viewModel(),
        onBackClick = { }
    )
}