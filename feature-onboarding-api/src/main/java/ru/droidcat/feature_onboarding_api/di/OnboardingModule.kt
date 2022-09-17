package ru.droidcat.feature_onboarding_api.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.droidcat.core_network_api.NetworkRepository
import ru.droidcat.feature_onboarding_api.usecase.SignUpUserUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnboardingModule {

    @Provides
    @Singleton
    fun provideSignUpUserUseCase(
        networkRepository: NetworkRepository
    ): SignUpUserUseCase {
        return SignUpUserUseCase(networkRepository)
    }
}