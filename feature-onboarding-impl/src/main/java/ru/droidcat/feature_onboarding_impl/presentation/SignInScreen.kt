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
import ru.droidcat.core_navigation.NavigateBack
import ru.droidcat.core_navigation.NavigationManager

@Composable
fun OnboardingSignInScreen(
    navigationManager: NavigationManager
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign In screen"
        )
        Button(
            onClick = {
                navigationManager.navigate(NavigateBack)
            }
        ) {
            Text("Go back")
        }
    }
}