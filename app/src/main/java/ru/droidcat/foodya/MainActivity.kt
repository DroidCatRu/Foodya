package ru.droidcat.foodya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.droidcat.core_navigation.NavigationCommand
import ru.droidcat.core_navigation.NavigationFactory
import ru.droidcat.core_navigation.NavigationManager
import ru.droidcat.core_ui.theme.FoodyaTheme
import ru.droidcat.core_utils.FeatureIntent
import ru.droidcat.core_utils.FeatureIntentManager
import ru.droidcat.core_utils.collectWithLifecycle
import ru.droidcat.feature_main_screen_impl.MainScreenDestination
import ru.droidcat.feature_onboarding_api.intents.UserSignedIntent
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationFactories: @JvmSuppressWildcards Set<NavigationFactory>

    @Inject
    lateinit var navigationManager: NavigationManager

    @Inject
    lateinit var featureIntentManager: FeatureIntentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            FoodyaTheme {
                val navController = rememberNavController()

                Surface {
                    NavigationHost(
                        navController = navController,
                        factories = navigationFactories
                    )
                }

                navigationManager
                    .navigationEvent
                    .collectWithLifecycle(
                        key = navController
                    ) { command ->
                        onNavCommand(command, navController)
                    }

                featureIntentManager
                    .featureIntent
                    .collectWithLifecycle(
                        key = navController
                    ) { intent ->
                        onFeatureIntent(intent)
                    }
            }
        }
    }

    private fun onNavCommand(command: NavigationCommand, navController: NavController) {
        if (command.navigateBack) {
            if (command.destination != null) {
                navController.popBackStack(
                    route = command.destination!!,
                    inclusive = false
                )
            } else {
                navController.popBackStack()
            }
        } else if (command.destination != null) {
            navController.navigate(
                route = command.destination!!,
            ) {
                command.configuration
                if (command.makeTop) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                        saveState = true
                    }
                }
            }
        }
    }

    private fun onFeatureIntent(intent: FeatureIntent) {
        if (intent is UserSignedIntent) {
            navigationManager.navigate(MainScreenDestination)
        }
    }
}