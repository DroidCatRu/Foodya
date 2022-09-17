package ru.droidcat.feature_onboarding_impl.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ru.droidcat.core_navigation.NavigationFactory
import ru.droidcat.feature_onboarding_impl.OnboardingNavigationFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface OnboardingModule {

    @Binds
    @Singleton
    @IntoSet
    fun bindOnboardingNavigationFactory(factory: OnboardingNavigationFactory): NavigationFactory
}