package ru.droidcat.feature_onboarding_impl.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.droidcat.core_utils.IoScope
import ru.droidcat.feature_onboarding_api.usecase.SignInUserUseCase
import javax.inject.Inject

@HiltViewModel
class SignInScreenViewModel @Inject constructor(
    private val signInUserUseCase: SignInUserUseCase,
    @IoScope private val scope: CoroutineScope
) : ViewModel() {

    private val _screenState = MutableStateFlow(SignInScreenState())
    val screenState: StateFlow<SignInScreenState> = _screenState

    fun inputEmail(email: String) {
        _screenState.value = _screenState.value.copy(emailFieldValue = email)
    }

    fun inputPassword(password: String) {
        _screenState.value = _screenState.value.copy(passwordFieldValue = password)
    }

    fun signInUser() {
        val state = _screenState.value
        if (state.emailFieldValue.isBlank()) {
            _screenState.value = _screenState.value.copy(emailError = "Введите почту")
        }
        if (state.passwordFieldValue.isBlank()) {
            _screenState.value = _screenState.value.copy(passwordError = "Введите пароль")
        }
        if (!textFieldsHasErrors()) {
            scope.launch {
                signInUserUseCase(
                    email = _screenState.value.emailFieldValue,
                    password = _screenState.value.passwordFieldValue
                )
            }
        }
    }

    private fun textFieldsHasErrors(): Boolean {
        if (_screenState.value.emailError.isNullOrBlank() &&
            _screenState.value.passwordError.isNullOrBlank()
        ) {
            return false
        }
        return true
    }
}

data class SignInScreenState(
    val globalError: String? = null,

    val emailFieldValue: String = "",
    val emailError: String? = null,

    val passwordFieldValue: String = "",
    val passwordError: String? = null,
)