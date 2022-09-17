package ru.droidcat.foodya.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.droidcat.core_navigation.NavigationManager
import ru.droidcat.foodya.NavigationManagerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Binds
    @Singleton
    fun bindNavigationManager(impl: NavigationManagerImpl): NavigationManager
}