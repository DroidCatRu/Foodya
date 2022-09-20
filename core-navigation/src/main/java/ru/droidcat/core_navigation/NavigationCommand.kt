package ru.droidcat.core_navigation

import androidx.navigation.NavOptions

interface NavigationCommand {
    val destination: String?
        get() = null

    val navigateBack: Boolean
        get() = false

    val configuration: NavOptions
        get() = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .build()
}

object NavigateBack: NavigationCommand {
    override val navigateBack = true
}