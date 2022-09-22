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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.*
import ru.droidcat.core_navigation.NavigateBack
import ru.droidcat.core_navigation.NavigationManager
import ru.droidcat.core_ui.components.buttons.FoodyaFilledButton
import ru.droidcat.core_utils.FeatureIntentManager
import ru.droidcat.feature_onboarding_api.intents.UserSignedIntent
import kotlin.math.roundToInt

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
fun OnboardingSignUpScreen(
    featureIntentManager: FeatureIntentManager,
    viewModel: SignUpScreenViewModel = hiltViewModel()
) {

    val screenState = viewModel.screenState.collectAsState()

    if (screenState.value.userSigned) {
        Log.d("Sign Up", "User signed up")
        LaunchedEffect(Unit) {
            featureIntentManager.sendIntent(UserSignedIntent)
        }
    }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val scope = rememberCoroutineScope()
    var scrollJob by remember { mutableStateOf<Job?>(null) }

    var nameTextField by remember { mutableStateOf<TextField>(TextField.NameField()) }
    var emailTextField by remember { mutableStateOf<TextField>(TextField.EmailField()) }
    var passwordTextField by remember { mutableStateOf<TextField>(TextField.PasswordField()) }

    var focusState by remember { mutableStateOf<FocusState>(FocusState.FocusEmpty) }
    var imeState by remember { mutableStateOf<ImeState>(ImeState.Hidden) }
    var paddingApplied by remember { mutableStateOf<TextField?>(null) }

    var totalOffset by remember { mutableStateOf(0.dp) }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    imeState = if (WindowInsets.isImeVisible) {
        ImeState.Open(WindowInsets.ime.asPaddingValues().calculateBottomPadding())
    } else {
        ImeState.Hidden
    }

    when (imeState) {
        is ImeState.Open -> {
            if (focusState is FocusState.Focused) {
                with(LocalDensity.current) {
                    val textField = (focusState as FocusState.Focused).field
                    val topOffset = (textField.topOffset + textField.size).toDp()
                    val bottomOffset = screenHeight - topOffset

                    if (paddingApplied == null ||
                        textField.javaClass != paddingApplied!!.javaClass
                    ) {
                        totalOffset =
                            (bottomOffset - (imeState as ImeState.Open).size
                                    + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                                    + WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                                    - 4.dp
                                    ).coerceAtMost(0.dp)

                        paddingApplied = textField

                        scrollJob?.cancel()
                        scrollJob = scope.launch {
                            scrollState.animateScrollTo(
                                scrollState.value - totalOffset.toPx().toInt()
                            )
                        }
                    }
                }
            }
        }
        is ImeState.Hidden -> {
            if (paddingApplied != null) {
                paddingApplied = null
                with(LocalDensity.current) {
                    scope.launch {
                        scrollState.animateScrollTo(
                            scrollState.value + totalOffset.toPx().toInt()
                        )
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    WindowInsets.systemBars.asPaddingValues()
                )
                .padding(
                    bottom =
                    if (paddingApplied != null) {
                        (0.dp - totalOffset).coerceAtLeast(0.dp)
                    } else {
                        0.dp
                    }
                )
                .padding(
                    horizontal = 16.dp
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
                        if (focusState != FocusState.Focused(nameTextField) &&
                            imeState is ImeState.Hidden
                        ) {
                            nameTextField = (nameTextField as TextField.NameField).copy(
                                topOffset = coordinates.positionInRoot().y.roundToInt(),
                                size = coordinates.size.height
                            )
                        }
                    }
                    .onFocusChanged {
                        if (it.isFocused || it.hasFocus) {
                            focusState = FocusState.Focused(nameTextField)
                        }
                    },
                value = screenState.value.nameFieldValue,
                onValueChange = {
                    viewModel.inputName(it)
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
                        if (focusState != FocusState.Focused(emailTextField) &&
                            imeState is ImeState.Hidden
                        ) {
                            emailTextField = (emailTextField as TextField.EmailField).copy(
                                topOffset = coordinates.positionInRoot().y.roundToInt(),
                                size = coordinates.size.height
                            )
                        }
                    }
                    .onFocusChanged {
                        if (it.isFocused || it.hasFocus) {
                            focusState = FocusState.Focused(emailTextField)
                        }
                    },
                value = screenState.value.emailFieldValue,
                onValueChange = {
                    viewModel.inputEmail(it)
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
                        if (focusState != FocusState.Focused(passwordTextField) &&
                            imeState is ImeState.Hidden
                        ) {
                            passwordTextField = (passwordTextField as TextField.PasswordField).copy(
                                topOffset = coordinates.positionInRoot().y.roundToInt(),
                                size = coordinates.size.height
                            )
                        }
                    }
                    .onFocusChanged {
                        if (it.isFocused || it.hasFocus) {
                            focusState = FocusState.Focused(passwordTextField)
                        }
                    },
                value = screenState.value.passwordFieldValue,
                onValueChange = {
                    viewModel.inputPassword(it)
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
                    onDone = { viewModel.signUpUser() }
                )
            )

            FoodyaFilledButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.signUpUser()
                }
            ) {
                Text("Создать аккаунт")
            }
        }
    }
}

sealed class TextField(
    open val topOffset: Int,
    open val size: Int
) {
    data class NameField(
        override val topOffset: Int = 0,
        override val size: Int = 0
    ) : TextField(topOffset, size)

    data class EmailField(
        override val topOffset: Int = 0,
        override val size: Int = 0
    ) : TextField(topOffset, size)

    data class PasswordField(
        override val topOffset: Int = 0,
        override val size: Int = 0
    ) : TextField(topOffset, size)
}

sealed class FocusState {
    data class Focused(val field: TextField) : FocusState()
    object FocusEmpty : FocusState()
}

sealed class ImeState {
    data class Open(val size: Dp) : ImeState()
    object Hidden : ImeState()
}