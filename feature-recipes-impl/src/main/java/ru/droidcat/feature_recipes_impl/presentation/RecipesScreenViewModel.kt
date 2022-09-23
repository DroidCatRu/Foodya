package ru.droidcat.feature_recipes_impl.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.droidcat.core_utils.IoScope
import ru.droidcat.feature_recipes_api.usecase.GetRecipesUseCase
import ru.droidcat.feature_recipes_api.usecase.Recipe
import javax.inject.Inject

@HiltViewModel
class RecipesScreenViewModel @Inject constructor(
    @IoScope private val scope: CoroutineScope,
    private val getRecipesUseCase: GetRecipesUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow(RecipesScreenState())
    val screenState: StateFlow<RecipesScreenState> = _screenState

    private var getRecipesJob: Job? = null

    init {
        getRecipes()
    }

    private fun getRecipes() {
        getRecipesJob?.cancel()
        getRecipesJob = scope.launch {
            val recipes = getRecipesUseCase()
            if (recipes != null && recipes.isNotEmpty()) {
                _screenState.value = _screenState.value.copy(recipes = recipes)
            }
        }
    }
}

data class RecipesScreenState(
    val recipes: List<Recipe> = listOf()
)