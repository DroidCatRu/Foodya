package ru.droidcat.foodya

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.*
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.droidcat.core_navigation.MainNavigationDestination
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
    lateinit var rootNavigationsSet: @JvmSuppressWildcards Set<MainNavigationDestination>

    @Inject
    lateinit var navigationManager: NavigationManager

    @Inject
    lateinit var featureIntentManager: FeatureIntentManager

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val rootNavigations = rootNavigationsSet.sortedBy { it.position }

        setContent {
            FoodyaTheme {
                val navController = rememberNavController()

                val destination =
                    navController.currentBackStackEntryAsState().value?.destination?.route

                Log.d("Destination", destination.toString())

                Surface {
                    Scaffold(
                        bottomBar = {
                            AnimatedVisibility(
                                visible =
                                rootNavigations.contains(
                                    destInFeature(rootNavigations, destination)
                                ),
                                enter = slideInVertically { fullHeight ->
                                    fullHeight
                                },
                                exit = slideOutVertically { fullHeight ->
                                    fullHeight
                                }
                            ) {
                                NavigationBar(
                                    windowInsets = WindowInsets.systemBars
                                        .only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)
                                ) {
                                    for (item in rootNavigations) {
                                        val featureSelected = isDestInFeature(item, destination)
                                        NavigationBarItem(
                                            icon = {
                                                Icon(
                                                    painterResource(id = item.iconResource),
                                                    contentDescription = item.label
                                                )
                                            },
                                            label = {
                                                Text(item.label)
                                            },
//                                            alwaysShowLabel = false,
                                            selected = featureSelected,
                                            onClick = {
                                                if (!featureSelected) {
                                                    navigationManager.navigate(item.navigationCommand)
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    ) {
                        NavigationHost(
                            navController = navController,
                            factories = navigationFactories
                        )
                    }
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
            if (!navController.popBackStack(
                    command.destination!!,
                    false
                )
            ) {
                navController.navigate(
                    route = command.destination!!,
                ) {
                    command.configuration
                    if (command.makeTop) {
                        val route = navController.graph.findStartDestination().route
                        if (route != null) {
                            popUpTo(route) {
                                inclusive = true
                                saveState = true
                            }
                        }
                    }
                }
            }
        }
    }

    private fun destInFeature(
        features: List<MainNavigationDestination>,
        destination: String?
    ): MainNavigationDestination? {
        features.forEach {
            if (isDestInFeature(it, destination)) {
                return it
            }
        }
        return null
    }

    private fun isDestInFeature(
        feature: MainNavigationDestination,
        destination: String?
    ): Boolean {
        return feature.featureDestinations.destinations.contains(
            destination
        )
    }

    private fun onFeatureIntent(intent: FeatureIntent) {
        if (intent is UserSignedIntent) {
            navigationManager.navigate(MainScreenDestination)
        }
    }
}