package ru.droidcat.feature_recipes_impl.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.droidcat.core_ui.components.cards.RecipeCard

@Composable
fun RecipesScreen(
    viewModel: RecipesScreenViewModel = hiltViewModel()
) {

    val state = viewModel.screenState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(
            top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding() + 24.dp,
            bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding() + 96.dp,
            start = WindowInsets.systemBars.asPaddingValues()
                .calculateStartPadding(LocalLayoutDirection.current) + 16.dp,
            end = WindowInsets.systemBars.asPaddingValues()
                .calculateEndPadding(LocalLayoutDirection.current) + 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(state.value.recipes) { recipe ->
            RecipeCard(
                recipeName = recipe.name,
                servings = recipe.servings,
                weight = recipe.weight,
                recipeImageUrl = recipe.imageUrl
            )
        }
    }
}