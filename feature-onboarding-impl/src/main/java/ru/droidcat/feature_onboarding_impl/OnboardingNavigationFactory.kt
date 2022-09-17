package ru.droidcat.feature_onboarding_impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.droidcat.core_navigation.NavigationDestination
import ru.droidcat.core_navigation.NavigationFactory
import ru.droidcat.core_navigation.NavigationManager
import ru.droidcat.feature_onboarding_impl.presentation.OnboardingAuthScreen
import ru.droidcat.feature_onboarding_impl.presentation.OnboardingGreetingScreen
import javax.inject.Inject

class OnboardingNavigationFactory @Inject constructor(
    private val navigationManager: NavigationManager
) : NavigationFactory {
    override fun create(builder: NavGraphBuilder) {
        builder.navigation(
            route = OnboardingDestination.route,
            startDestination = OnboardingGreeting.route
        ) {
            composable(route = OnboardingGreeting.route) {
                OnboardingGreetingScreen(navigationManager)
            }
            composable(route = OnboardingAuth.route) {
                OnboardingAuthScreen(navigationManager)
            }
        }
//        builder.composable(OnboardingDestination.route) {
//            OnboardingScreen()
//        }
    }
}

object OnboardingDestination : NavigationDestination("onboardingDestination")
object OnboardingGreeting : NavigationDestination("onboardingGreeting")
object OnboardingAuth : NavigationDestination("onboardingAuth")