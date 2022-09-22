package ru.droidcat.feature_main_screen_impl.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.droidcat.core_utils.IoScope
import ru.droidcat.feature_main_screen_api.usecase.GetUserNameUseCase
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    @IoScope private val scope: CoroutineScope,
    private val getUserNameUseCase: GetUserNameUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow(MainScreenState())
    val screenState: StateFlow<MainScreenState> = _screenState

    private var getNameJob: Job? = null

    init {
        getUserName()
    }

    private fun getUserName() {
        getNameJob?.cancel()
        getNameJob = scope.launch {
            val nameFromNet = getUserNameUseCase()
            if (!nameFromNet.isNullOrBlank()) {
                _screenState.value = _screenState.value.copy(userName = nameFromNet)
            }
        }
    }
}

data class MainScreenState(
    val userName: String = "username"
)