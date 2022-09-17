package ru.droidcat.foodya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
        setContent {
            FoodyaTheme {
                val navController = rememberNavController()

                NavigationHost(
                    navController = navController,
                    factories = navigationFactories
                )

                navigationManager
                    .navigationEvent
                    .collectWithLifecycle(
                        key = navController
                    ) {
                        navController.navigate(it.destination, it.configuration)
                    }
            }
        }
    }
}