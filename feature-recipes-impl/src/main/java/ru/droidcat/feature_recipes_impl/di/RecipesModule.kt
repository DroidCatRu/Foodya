package ru.droidcat.feature_recipes_impl.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import ru.droidcat.core_navigation.MainNavigationDestination
import ru.droidcat.core_navigation.NavigationFactory
import ru.droidcat.feature_recipes_impl.RecipesNavigationFactory
import ru.droidcat.feature_recipes_impl.RecipesScreenNavigationDestination
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RecipesModule {

    @Binds
    @Singleton
    @IntoSet
    fun bindRecipesScreenNavigationDestination(dest: RecipesScreenNavigationDestination): MainNavigationDestination

    @Binds
    @Singleton
    @IntoSet
    fun bindRecipesNavigationFactory(factory: RecipesNavigationFactory): NavigationFactory
}