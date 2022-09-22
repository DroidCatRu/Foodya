package ru.droidcat.foodya

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.droidcat.core_navigation.NavigationDestination
import ru.droidcat.core_navigation.NavigationFactory
import ru.droidcat.feature_onboarding_impl.OnboardingDestination

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    factories: Set<NavigationFactory>
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = OnboardingDestination.destination
    ) {
        factories.forEach {
            it.create(this)
        }
    }
}