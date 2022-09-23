package ru.droidcat.feature_main_screen_api.usecase

import ru.droidcat.core_db_api.DatabaseRepository
import ru.droidcat.core_network_api.NetworkRepository
import javax.inject.Inject

class GetUserNameUseCase @Inject constructor(
    private val dbRepository: DatabaseRepository,
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke(): String? {
        var userName = dbRepository.getUserName()

        if (!userName.isNullOrBlank()) {
            return userName
        }

        val dbId = dbRepository.getUserDatabaseId()

        if (dbId.isNullOrBlank()) {
            return null
        }

        userName = networkRepository.getUserProfile(dbId)?.name

        if (!userName.isNullOrBlank()) {
            dbRepository.saveUserName(userName)
            return userName
        }

        return null
    }
}