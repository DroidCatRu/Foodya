package ru.droidcat.feature_onboarding_api.usecase

import ru.droidcat.core_db_api.DatabaseRepository
import ru.droidcat.core_network_api.MutationResult
import ru.droidcat.core_network_api.NetworkRepository
import ru.droidcat.feature_onboarding_api.SignResults
import javax.inject.Inject

class SignInUserUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val dbRepository: DatabaseRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): SignResults {
        val networkResult = networkRepository.signInUserEmailPassword(email, password)

        if (networkResult is MutationResult.ERROR) {
            return SignResults.ERROR.UNKNOWN
        }

        val userId = (networkResult as MutationResult.SUCCESS).data.toString()
        val userData = networkRepository.getUserProfile(userId) ?: return SignResults.ERROR.UNKNOWN

        val dbResult =
            dbRepository.saveUserDatabaseId(userId) && dbRepository.saveUserName(userData.name)

        if (!dbResult) {
            return SignResults.ERROR.DB.WRITE_ERROR
        }

        return SignResults.SUCCESS
    }
}