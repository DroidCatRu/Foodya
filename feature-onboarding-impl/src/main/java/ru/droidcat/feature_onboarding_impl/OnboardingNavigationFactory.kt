package ru.droidcat.feature_onboarding_impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.droidcat.core_navigation.NavigationCommand
import ru.droidcat.core_navigation.NavigationDestination
import ru.droidcat.core_navigation.NavigationFactory
import ru.droidcat.core_navigation.NavigationManager
import ru.droidcat.core_utils.FeatureIntentManager
import ru.droidcat.feature_onboarding_impl.presentation.OnboardingGreetingScreen
import ru.droidcat.feature_onboarding_impl.presentation.OnboardingSignInScreen
import ru.droidcat.feature_onboarding_impl.presentation.OnboardingSignUpScreen
import javax.inject.Inject

class OnboardingNavigationFactory @Inject constructor(
    private val navigationManager: NavigationManager,
    private val featureIntentManager: FeatureIntentManager
) : NavigationFactory {
    override fun create(builder: NavGraphBuilder) {
        builder.navigation(
            route = OnboardingDestination.destination,
            startDestination = OnboardingGreeting.destination
        ) {
            composable(route = OnboardingGreeting.destination) {
                OnboardingGreetingScreen(navigationManager, featureIntentManager)
            }
            composable(route = OnboardingSignIn.destination) {
                OnboardingSignInScreen(featureIntentManager)
            }
            composable(route = OnboardingSignUp.destination) {
                OnboardingSignUpScreen(featureIntentManager)
            }
        }
    }
}

object OnboardingDestination : NavigationCommand {
    override val destination = "onboardingDestination"
}

object OnboardingGreeting : NavigationCommand {
    override val destination = "onboardingGreeting"
}

object OnboardingSignIn : NavigationCommand {
    override val destination = "onboardingSignIn"
}

object OnboardingSignUp : NavigationCommand {
    override val destination = "onboardingSignUp"
}