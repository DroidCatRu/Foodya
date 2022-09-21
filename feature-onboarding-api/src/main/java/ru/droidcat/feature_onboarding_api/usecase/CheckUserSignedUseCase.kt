package ru.droidcat.feature_onboarding_api.usecase

import ru.droidcat.core_db_api.DatabaseRepository

class CheckUserSignedUseCase(
    private val dbRepository: DatabaseRepository
) {
    suspend operator fun invoke(): Boolean {
        return !dbRepository.getUserDatabaseId().isNullOrBlank()
    }
}