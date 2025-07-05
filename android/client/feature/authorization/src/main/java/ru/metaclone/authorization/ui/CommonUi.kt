package ru.metaclone.authorization.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import ru.metaclone.theme.AppIcons
import ru.metaclone.theme.AppStrings

@Composable
fun UiAuthFormTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChanged,
        label = { Text(label) },
        singleLine = true
    )
}

@Composable
internal fun UiAuthFormPasswordField(
    modifier: Modifier = Modifier,
    label: String = stringResource(AppStrings.password),
    value: String,
    onValueChanged: (String) -> Unit,
    isError: Boolean = false,
    errorText: String = "error"
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            label = {
                Text(text = label)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            value = value,
            onValueChange = onValueChanged,
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(AppIcons.show)
                else
                    painterResource(AppIcons.hide)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, contentDescription = null)
                }
            },
            isError = isError,
            modifier = Modifier.fillMaxWidth()
        )
        if (isError) {
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
internal fun UiAuthButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = text
        )
    }
}