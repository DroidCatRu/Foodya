package ru.droidcat.feature_products_impl.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ru.droidcat.core_navigation.MainNavigationDestination
import ru.droidcat.core_navigation.NavigationFactory
import ru.droidcat.feature_products_impl.ProductsNavigationFactory
import ru.droidcat.feature_products_impl.ProductsScreenNavigationDestination
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ProductsModule {

    @Binds
    @Singleton
    @IntoSet
    fun bindProductsScreenNavigationDestination(dest: ProductsScreenNavigationDestination): MainNavigationDestination

    @Binds
    @Singleton
    @IntoSet
    fun bindProductsNavigationFactory(factory: ProductsNavigationFactory): NavigationFactory
}