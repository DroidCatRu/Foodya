package ru.droidcat.core_navigation

import androidx.annotation.DrawableRes

abstract class MainNavigationDestination(
    val label: String,
    @DrawableRes val iconResource: Int,
    val navigationCommand: NavigationCommand,
    val featureDestinations: FeatureDestinations,
    val position: Int = 0
)