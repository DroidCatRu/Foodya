package ru.droidcat.feature_main_screen_impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import ru.droidcat.core_navigation.*
import ru.droidcat.core_utils.FeatureIntentManager
import ru.droidcat.feature_main_screen_impl.presentation.MainScreen
import javax.inject.Inject

class MainScreenNavigationFactory @Inject constructor(
    private val navigationManager: NavigationManager,
    private val featureIntentManager: FeatureIntentManager
) : NavigationFactory {
    override fun create(builder: NavGraphBuilder) {
        builder.composable(MainScreenDestination.destination) {
            MainScreen()
        }
    }
}

class MainScreenNavigationDestination @Inject constructor() : MainNavigationDestination(
    label = "Главная",
    iconResource = ru.droidcat.core_ui.R.drawable.cottage,
    navigationCommand = MainScreenDestination,
    featureDestinations = MainNavigationFeatureDestinations,
    position = 0
)

object MainScreenDestination : NavigationCommand {
    override val destination = "mainScreenDestination"
    override val makeTop = true
}

object MainNavigationFeatureDestinations : FeatureDestinations(
    destinations = listOf(MainScreenDestination.destination)
)