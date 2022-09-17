package ru.droidcat.feature_onboarding_impl.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.droidcat.core_navigation.NavigationCommand
import ru.droidcat.core_navigation.NavigationManager
import ru.droidcat.feature_onboarding_impl.OnboardingAuth
import ru.droidcat.feature_onboarding_impl.OnboardingGreeting

@Composable
fun OnboardingGreetingScreen(
    navigationManager: NavigationManager
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hello there, you're in Foodya Foodtracker"
        )
        Button(
            onClick = {
                navigationManager.navigate(
                    object: NavigationCommand {
                        override val destination = OnboardingAuth.route
                    }
                )
            }
        ) {
            Text("Authenticate")
        }
    }
}