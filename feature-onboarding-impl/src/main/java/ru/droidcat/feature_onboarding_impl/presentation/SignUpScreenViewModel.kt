package ru.droidcat.feature_onboarding_impl.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.droidcat.core_utils.IoScope
import ru.droidcat.feature_onboarding_api.SignResults
import ru.droidcat.feature_onboarding_api.usecase.SignUpUserUseCase
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    private val signUpUserUseCase: SignUpUserUseCase,
    @IoScope private val scope: CoroutineScope
): ViewModel() {

    private val _screenState = MutableStateFlow(SignUpScreenState())
    val screenState: StateFlow<SignUpScreenState> = _screenState

    fun inputName(name: String) {
        _screenState.value = _screenState.value.copy(nameFieldValue = name)
    }

    fun inputEmail(email: String) {
        _screenState.value = _screenState.value.copy(emailFieldValue = email)
    }

    fun inputPassword(password: String) {
        _screenState.value = _screenState.value.copy(passwordFieldValue = password)
    }

    fun signUpUser() {
        val state = _screenState.value
        if (state.nameFieldValue.isBlank()) {
            _screenState.value = _screenState.value.copy(nameError = "Введите имя")
        }
        if (state.emailFieldValue.isBlank()) {
            _screenState.value = _screenState.value.copy(emailError = "Введите почту")
        }
        if (state.passwordFieldValue.isBlank()) {
            _screenState.value = _screenState.value.copy(passwordError = "Введите пароль")
        }
        if (!textFieldsHasErrors()) {
            scope.launch {
                val signResult = signUpUserUseCase(
                    name = _screenState.value.nameFieldValue,
                    email = _screenState.value.emailFieldValue,
                    password = _screenState.value.passwordFieldValue
                )
                if (signResult is SignResults.SUCCESS) {
                    _screenState.value = _screenState.value.copy(userSigned = true)
                } else {
                    Log.d("Sign Up", signResult.toString())
                }
            }
        }
    }

    private fun textFieldsHasErrors(): Boolean {
        if (_screenState.value.nameError.isNullOrBlank() &&
                _screenState.value.emailError.isNullOrBlank() &&
                _screenState.value.passwordError.isNullOrBlank()) {
            return false
        }
        return true
    }
}

data class SignUpScreenState(
    val userSigned: Boolean = false,

    val globalError: String? = null,

    val nameFieldValue: String = "",
    val nameError: String? = null,

    val emailFieldValue: String = "",
    val emailError: String? = null,

    val passwordFieldValue: String = "",
    val passwordError: String? = null,
)