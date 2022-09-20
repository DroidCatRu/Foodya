package ru.droidcat.feature_onboarding_impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.droidcat.core_navigation.NavigationDestination
import ru.droidcat.core_navigation.NavigationFactory
import ru.droidcat.core_navigation.NavigationManager
import ru.droidcat.feature_onboarding_impl.presentation.OnboardingGreetingScreen
import ru.droidcat.feature_onboarding_impl.presentation.OnboardingSignInScreen
import ru.droidcat.feature_onboarding_impl.presentation.OnboardingSignUpScreen
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
            composable(route = OnboardingSignIn.route) {
                OnboardingSignInScreen(navigationManager)
            }
            composable(route = OnboardingSignUp.route) {
                OnboardingSignUpScreen(navigationManager)
            }
        }
    }
}

object OnboardingDestination : NavigationDestination("onboardingDestination")
object OnboardingGreeting : NavigationDestination("onboardingGreeting")
object OnboardingSignIn : NavigationDestination("onboardingSignIn")
object OnboardingSignUp : NavigationDestination("onboardingSignUp")