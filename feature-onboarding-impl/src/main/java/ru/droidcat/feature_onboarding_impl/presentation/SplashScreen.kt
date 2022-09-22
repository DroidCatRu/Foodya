package ru.droidcat.feature_onboarding_impl.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = spacedBy(48.dp)
        ) {
            Icon(
                modifier = Modifier.size(192.dp),
                painter = painterResource(id = ru.droidcat.core_ui.R.drawable.cookie),
                contentDescription = "App Logo",
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Foodya",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}