package ru.droidcat.foodya

import androidx.annotation.DrawableRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.droidcat.core_navigation.FeatureDestinations
import ru.droidcat.core_navigation.NavigationCommand
import ru.droidcat.core_navigation.NavigationManager
import ru.droidcat.core_utils.MainImmediateDispatcher
import ru.droidcat.core_utils.MainImmediateScope
import ru.droidcat.feature_main_screen_impl.MainNavigationFeatureDestinations
import ru.droidcat.feature_main_screen_impl.MainScreenDestination
import ru.droidcat.feature_products_impl.ProductsNavigationFeatureDestinations
import ru.droidcat.feature_products_impl.ProductsScreenDestination
import javax.inject.Inject

class NavigationManagerImpl @Inject constructor(
    @MainImmediateScope private val externalMainImmediateScope: CoroutineScope
) : NavigationManager {

    private val navigationCommandChannel = Channel<NavigationCommand>(Channel.BUFFERED)
    override val navigationEvent = navigationCommandChannel.receiveAsFlow()

    override fun navigate(command: NavigationCommand) {
        externalMainImmediateScope.launch {
            navigationCommandChannel.send(command)
        }
    }
}