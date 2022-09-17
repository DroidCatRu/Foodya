package ru.droidcat.feature_onboarding_api.usecase

import ru.droidcat.core_network_api.MutationResult
import ru.droidcat.core_network_api.NetworkRepository

class SignUpUserUseCase(
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): MutationResult {
        return networkRepository.signUpUserEmailPassword(
            name,
            email,
            password
        )
    }
}