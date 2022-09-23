package ru.droidcat.feature_recipes_impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.droidcat.core_navigation.*
import ru.droidcat.core_utils.FeatureIntentManager
import ru.droidcat.feature_recipes_impl.presentation.RecipesScreen
import javax.inject.Inject

class RecipesNavigationFactory @Inject constructor(
    private val navigationManager: NavigationManager,
    private val featureIntentManager: FeatureIntentManager
) : NavigationFactory {

    override fun create(builder: NavGraphBuilder) {
        builder.composable(
            route = RecipesScreenDestination.destination
        ) {
            RecipesScreen()
        }
    }
}

class RecipesScreenNavigationDestination @Inject constructor() : MainNavigationDestination(
    label = "Рецепты",
    iconResource = ru.droidcat.core_ui.R.drawable.menu_book,
    navigationCommand = RecipesScreenDestination,
    featureDestinations = RecipesNavigationFeatureDestinations,
    position = 2
)

object RecipesScreenDestination : NavigationCommand {
    override val destination = "recipesDestination"
    override val makeTop = true
}

object RecipesNavigationFeatureDestinations : FeatureDestinations(
    destinations = listOf(RecipesScreenDestination.destination)
)