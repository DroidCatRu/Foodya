package ru.droidcat.feature_onboarding_api.usecase

import ru.droidcat.core_db_api.DatabaseRepository
import javax.inject.Inject

class CheckUserSignedUseCase @Inject constructor(
    private val dbRepository: DatabaseRepository
) {
    suspend operator fun invoke(): Boolean {
        return !dbRepository.getUserDatabaseId().isNullOrBlank()
    }
}