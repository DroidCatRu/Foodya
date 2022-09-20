package ru.droidcat.foodya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.droidcat.core_navigation.NavigationFactory
import ru.droidcat.core_navigation.NavigationManager
import ru.droidcat.core_ui.theme.FoodyaTheme
import ru.droidcat.core_utils.collectWithLifecycle
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationFactories: @JvmSuppressWildcards Set<NavigationFactory>

    @Inject
    lateinit var navigationManager: NavigationManager

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
                                command.destination!!,
                                command.configuration
                            )
                        }
                    }
            }
        }
    }
}