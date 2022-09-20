package ru.droidcat.feature_onboarding_impl.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.droidcat.core_navigation.NavigationCommand
import ru.droidcat.core_navigation.NavigationManager
import ru.droidcat.core_ui.components.buttons.FoodyaFilledButton
import ru.droidcat.core_ui.components.buttons.FoodyaTextButton
import ru.droidcat.feature_onboarding_impl.OnboardingSignIn
import ru.droidcat.feature_onboarding_impl.OnboardingSignUp

@Composable
fun OnboardingGreetingScreen(
    navigationManager: NavigationManager
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = spacedBy(8.dp)
        ) {
            Text(
                text = "Добро пожаловать",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "У вас уже есть аккаунт?",
                style = MaterialTheme.typography.titleMedium
            )

            FoodyaFilledButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navigationManager.navigate(
                        object: NavigationCommand {
                            override val destination = OnboardingSignIn.route
                        }
                    )
                }
            ) {
                Text("Да, войти")
            }

            FoodyaTextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navigationManager.navigate(
                        object: NavigationCommand {
                            override val destination = OnboardingSignUp.route
                        }
                    )
                }
            ) {
                Text("Нет, создать")
            }
        }
    }
}