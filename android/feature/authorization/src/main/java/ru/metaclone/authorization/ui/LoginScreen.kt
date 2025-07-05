package ru.metaclone.authorization.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import ru.metaclone.authorization.viewmodel.LoginViewModel
import ru.metaclone.theme.AppDimens
import ru.metaclone.theme.AppStrings

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = koinViewModel(),
    onRegisterNavigate: (String) -> Unit
) {
    var loginValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }

    val context = LocalContext.current
    val incorrectFieldsText = stringResource(AppStrings.incorrectFields)

    UiLoginForm(
        loginValue = loginValue,
        onLoginChanged = { loginValue = it },
        passwordValue = passwordValue,
        onPasswordChanged = { passwordValue = it },
        onLoginClick = {
            if (loginValue.isNotBlank() && passwordValue.isNotBlank()) {
                loginViewModel.login(
                    login = loginValue,
                    password = passwordValue
                )
            } else {
                Toast.makeText(context, incorrectFieldsText, Toast.LENGTH_SHORT).show()
            }
        },
        onRegisterClick = {
            onRegisterNavigate(loginValue)
        }
    )
}

@Composable
fun UiLoginForm(
    loginValue: String,
    onLoginChanged: (String) -> Unit,
    passwordValue: String,
    onPasswordChanged: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    val loginFormModifier = Modifier
        .padding(top = 24.dp)
        .fillMaxWidth()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AppDimens.MAIN_HORIZONTAL_PADDING)
    ) {
        Text(
            text = stringResource(AppStrings.welcome),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        UiAuthFormTextField(
            modifier = loginFormModifier,
            label = stringResource(AppStrings.login),
            value = loginValue,
            onValueChanged = onLoginChanged
        )
        UiAuthFormPasswordField(
            modifier = loginFormModifier,
            value = passwordValue,
            onValueChanged = onPasswordChanged
        )
        Row(
            modifier = loginFormModifier
        ) {
            Text(text = stringResource(AppStrings.newUserText))
            Text(
                text = stringResource(AppStrings.register),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable(onClick = onRegisterClick)
            )
        }
        UiAuthButton(
            modifier = loginFormModifier,
            text = stringResource(AppStrings.login),
            onClick = onLoginClick
        )
    }
}

@Preview
@Composable
private fun LoginPreview() {
    UiLoginForm(
        loginValue = "PreviewLogin",
        onLoginChanged = {},
        passwordValue = "PreviewPassword",
        onPasswordChanged = {},
        onRegisterClick = {},
        onLoginClick = {}
    )
}
