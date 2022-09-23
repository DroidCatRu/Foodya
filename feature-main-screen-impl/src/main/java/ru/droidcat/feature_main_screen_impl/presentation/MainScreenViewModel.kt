package ru.droidcat.feature_main_screen_impl.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.droidcat.core_utils.IoScope
import ru.droidcat.feature_main_screen_api.usecase.*
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    @IoScope private val scope: CoroutineScope,
    private val getUserNameUseCase: GetUserNameUseCase,
    private val getUserHydrationUseCase: GetUserHydrationUseCase,
    private val addWaterUseCase: AddWaterUseCase,
    private val decreaseWaterUseCase: DecreaseWaterUseCase,
    private val getRecommendedRecipesUseCase: GetRecommendedRecipesUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow(MainScreenState())
    val screenState: StateFlow<MainScreenState> = _screenState

    private var getNameJob: Job? = null
    private var getHydrationJob: Job? = null
    private var getRecipesJob: Job? = null

    init {
        getUserName()
        getHydration()
        getRecipes()
    }

    fun addWater() {
        scope.launch {
            addWaterUseCase()
            getHydration()
        }
    }

    fun decreaseWater() {
        scope.launch {
            decreaseWaterUseCase()
            getHydration()
        }
    }

    private fun getUserName() {
        getNameJob?.cancel()
        getNameJob = scope.launch {
            val name = getUserNameUseCase()
            if (!name.isNullOrBlank()) {
                _screenState.value = _screenState.value.copy(userName = name)
            }
        }
    }

    private fun getHydration() {
        getHydrationJob?.cancel()
        getHydrationJob = scope.launch {
            val hydration = getUserHydrationUseCase()
            if (hydration != null) {
                _screenState.value = _screenState.value.copy(hydration = hydration)
            }

        }
    }

    private fun getRecipes() {
        getRecipesJob?.cancel()
        getRecipesJob = scope.launch {
            val recipes = getRecommendedRecipesUseCase()
            if (recipes != null && recipes.isNotEmpty()) {
                _screenState.value = _screenState.value.copy(recipes = recipes)
            }
        }
    }
}

data class MainScreenState(
    val userName: String = "\u2588\u2588\u2588\u2588\u2588\u2588",
    val hydration: Hydration? = null,
    val recipes: List<Recipe> = listOf()
)