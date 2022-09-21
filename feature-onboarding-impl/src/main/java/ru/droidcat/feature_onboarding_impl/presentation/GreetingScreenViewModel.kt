package ru.droidcat.feature_onboarding_impl.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.droidcat.core_utils.IoScope
import ru.droidcat.feature_onboarding_api.usecase.CheckUserSignedUseCase
import javax.inject.Inject

@HiltViewModel
class GreetingScreenViewModel @Inject constructor(
    private val checkUserSignedUseCase: CheckUserSignedUseCase,
    @IoScope private val ioScope: CoroutineScope
): ViewModel() {

    private var checkUserJob: Job? = null
    private var _userSigned = MutableStateFlow<Boolean?>(null)
    val userSigned: StateFlow<Boolean?> = _userSigned

    init {
        checkUserSigned()
    }

    private fun checkUserSigned() {
        checkUserJob?.cancel()
        checkUserJob = ioScope.launch {
            delay(1000)
            _userSigned.value = checkUserSignedUseCase()
        }
    }
}