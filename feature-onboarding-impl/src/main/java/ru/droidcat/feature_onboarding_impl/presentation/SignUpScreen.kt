package ru.droidcat.feature_onboarding_impl.presentation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import ru.droidcat.core_navigation.NavigateBack
import ru.droidcat.core_navigation.NavigationManager
import ru.droidcat.core_ui.components.buttons.FoodyaFilledButton

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class
)
@Composable
fun OnboardingSignUpScreen(
    navigationManager: NavigationManager
) {

    var nameValue by rememberSaveable { mutableStateOf("") }
    var emailValue by rememberSaveable { mutableStateOf("") }
    var passwordValue by rememberSaveable { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    var nameFieldOffset by remember { mutableStateOf(Offset.Zero) }
    var emailFieldOffset by remember { mutableStateOf(Offset.Zero) }
    var passwordFieldOffset by remember { mutableStateOf(Offset.Zero) }

    var nameFieldFocused by remember { mutableStateOf(false) }
    var emailFieldFocused by remember { mutableStateOf(false) }
    var passwordFieldFocused by remember { mutableStateOf(false) }

    var focusChanged by remember { mutableStateOf(false) }
    var done by remember { mutableStateOf(false) }

    if (focusChanged) {
        done = false
        focusChanged = false
    }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val topOffset =
        if (nameFieldFocused) {
            with(LocalDensity.current) {
                nameFieldOffset.y.toDp()
            }
        } else if (emailFieldFocused) {
            with(LocalDensity.current) {
                emailFieldOffset.y.toDp()
            }
        } else if (passwordFieldFocused) {
            with(LocalDensity.current) {
                passwordFieldOffset.y.toDp()
            }
        } else {
            screenHeight
        }

    val bottomOffset = screenHeight - topOffset

    val focused = WindowInsets.isImeVisible &&
            (nameFieldFocused || emailFieldFocused || passwordFieldFocused)

    val totalOffset =
        if (!focused && !done) {
            0.dp
        } else if (!focused && done) {
            done = false
            0.dp
        } else {
            done = true
            (bottomOffset - WindowInsets.ime.asPaddingValues().calculateBottomPadding() +
                    WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() +
                    WindowInsets.statusBars.asPaddingValues().calculateTopPadding() - 4.dp)
                .coerceAtMost(0.dp)
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    WindowInsets.systemBars.asPaddingValues()
                )
                .padding(
                    horizontal = 16.dp
                )
                .offset(
                    y = totalOffset
                ),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = spacedBy(8.dp)
        ) {
            Text(
                text = "Давайте знакомиться",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Заполните ниже свое имя, email и придумайте надежный пароль",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        if (!nameFieldFocused) {
                            nameFieldOffset = coordinates.positionInRoot() +
                                    Offset(0f, coordinates.size.height.toFloat())
                        }
                    }
                    .onFocusChanged {
                        nameFieldFocused = (it.isFocused || it.hasFocus)
                        focusChanged = true
                    },
                value = nameValue,
                onValueChange = {
                    nameValue = it
                },
                label = {
                    Text("Имя")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        if (!emailFieldFocused) {
                            emailFieldOffset = coordinates.positionInRoot() +
                                    Offset(0f, coordinates.size.height.toFloat())
                        }
                    }
                    .onFocusChanged {
                        emailFieldFocused = (it.isFocused || it.hasFocus)
                        focusChanged = true
                    },
                value = emailValue,
                onValueChange = {
                    emailValue = it
                },
                label = {
                    Text("email")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        if (!passwordFieldFocused) {
                            passwordFieldOffset = coordinates.positionInRoot() +
                                    Offset(0f, coordinates.size.height.toFloat())
                            Log.d("Offset pos", "$passwordFieldOffset")
                        }
                    }
                    .onFocusChanged {
                        passwordFieldFocused = (it.isFocused || it.hasFocus)
                        Log.d("Offset focused", "$passwordFieldOffset")
                        focusChanged = true
                    },
                value = passwordValue,
                onValueChange = {
                    passwordValue = it
                },
                label = {
                    Text("Пароль")
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = { createUser(navigationManager) }
                )
            )

            FoodyaFilledButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    createUser(navigationManager)
                }
            ) {
                Text("Создать аккаунт")
            }
        }
    }
}

private fun navigateBack(navigationManager: NavigationManager) {
    navigationManager.navigate(NavigateBack)
}

private fun createUser(navigationManager: NavigationManager) {
    navigateBack(navigationManager)
}