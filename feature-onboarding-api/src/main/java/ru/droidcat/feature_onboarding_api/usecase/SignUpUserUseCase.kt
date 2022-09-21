package ru.droidcat.feature_onboarding_api.usecase

import ru.droidcat.core_db_api.DatabaseRepository
import ru.droidcat.core_network_api.MutationResult
import ru.droidcat.core_network_api.NetworkRepository
import ru.droidcat.feature_onboarding_api.SignResults

class SignUpUserUseCase(
    private val networkRepository: NetworkRepository,
    private val dbRepository: DatabaseRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): SignResults {

        val networkResult = networkRepository.signUpUserEmailPassword(name, email, password)

        if (networkResult is MutationResult.ERROR){
            return SignResults.ERROR.UNKNOWN
        }

        val dbResult =
            dbRepository.saveUserDatabaseId((networkResult as MutationResult.SUCCESS).data.toString())

        return if (dbResult) SignResults.SUCCESS else SignResults.ERROR.DB.WRITE_ERROR
    }
}