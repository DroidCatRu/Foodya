package ru.droidcat.foodya.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.droidcat.core_navigation.NavigationManager
import ru.droidcat.core_utils.FeatureIntentManager
import ru.droidcat.foodya.FeatureIntentManagerImpl
import ru.droidcat.foodya.NavigationManagerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface MainModule {

    @Binds
    @Singleton
    fun bindNavigationManager(impl: NavigationManagerImpl): NavigationManager

    @Binds
    @Singleton
    fun bindFeatureIntentManager(impl: FeatureIntentManagerImpl): FeatureIntentManager
}