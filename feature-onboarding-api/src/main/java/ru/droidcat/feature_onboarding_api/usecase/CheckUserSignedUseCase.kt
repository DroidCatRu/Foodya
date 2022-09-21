package ru.droidcat.feature_onboarding_api.usecase

import ru.droidcat.core_db_api.DatabaseRepository
import ru.droidcat.core_network_api.NetworkRepository

class CheckUserSignedUseCase(
    private val dbRepository: DatabaseRepository,
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke(): Boolean {
        return !dbRepository.getUserDatabaseId().isNullOrBlank()
    }
}