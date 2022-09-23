package ru.droidcat.feature_main_screen_impl.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ru.droidcat.core_navigation.MainNavigationDestination
import ru.droidcat.core_navigation.NavigationFactory
import ru.droidcat.feature_main_screen_impl.MainScreenNavigationDestination
import ru.droidcat.feature_main_screen_impl.MainScreenNavigationFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface MainScreenModule {

    @Binds
    @Singleton
    @IntoSet
    fun bindMainScreenNavigationDestination(dest: MainScreenNavigationDestination): MainNavigationDestination

    @Binds
    @Singleton
    @IntoSet
    fun bindMainScreenNavigationFactory(factory: MainScreenNavigationFactory): NavigationFactory
}