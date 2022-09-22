package ru.droidcat.feature_main_screen_api.usecase

import ru.droidcat.core_db_api.DatabaseRepository
import ru.droidcat.core_network_api.NetworkRepository
import javax.inject.Inject

class GetUserNameUseCase @Inject constructor(
    private val dbRepository: DatabaseRepository,
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke(): String? {
        val dbId = dbRepository.getUserDatabaseId()
        if (dbId.isNullOrBlank()) {
            return null
        }
        return networkRepository.getUserProfile(dbId)?.name
    }
}