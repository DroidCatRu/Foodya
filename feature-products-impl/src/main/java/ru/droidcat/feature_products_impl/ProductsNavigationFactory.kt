package ru.droidcat.feature_products_impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.droidcat.core_navigation.*
import ru.droidcat.core_utils.FeatureIntentManager
import ru.droidcat.feature_products_impl.presentation.ProductsScreen
import javax.inject.Inject

class ProductsNavigationFactory @Inject constructor(
    private val navigationManager: NavigationManager,
    private val featureIntentManager: FeatureIntentManager
) : NavigationFactory {
    override fun create(builder: NavGraphBuilder) {
        builder.composable(
            route = ProductsScreenDestination.destination
        ) {
            ProductsScreen()
        }
    }
}

class ProductsScreenNavigationDestination @Inject constructor() : MainNavigationDestination(
    label = "Продукты",
    iconResource = ru.droidcat.core_ui.R.drawable.egg,
    navigationCommand = ProductsScreenDestination,
    featureDestinations = ProductsNavigationFeatureDestinations,
    position = 1
)

object ProductsScreenDestination : NavigationCommand {
    override val destination = "productsDestination"
    override val makeTop = true
}

object ProductsNavigationFeatureDestinations : FeatureDestinations(
    destinations = listOf(ProductsScreenDestination.destination)
)