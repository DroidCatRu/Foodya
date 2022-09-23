package ru.droidcat.feature_onboarding_api.usecase

import ru.droidcat.core_db_api.DatabaseRepository
import ru.droidcat.core_network_api.MutationResult
import ru.droidcat.core_network_api.NetworkRepository
import ru.droidcat.feature_onboarding_api.SignResults
import javax.inject.Inject

class SignUpUserUseCase @Inject constructor(
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
            return when (networkResult) {
                is MutationResult.ERROR.AUTH.USER_ALREADY_SIGNED_IN -> {
                    SignResults.ERROR.USER_ALREADY_SIGNED_IN
                }
                is MutationResult.ERROR.AUTH.USER_ALREADY_EXISTS -> {
                    SignResults.ERROR.USER_ALREADY_EXISTS
                }
                is MutationResult.ERROR.AUTH.WEAK_PASSWORD -> {
                    SignResults.ERROR.WEAK_PASSWORD
                }
                else -> {
                    SignResults.ERROR.UNKNOWN
                }
            }
        }



        val dbResult =
            dbRepository.saveUserDatabaseId((networkResult as MutationResult.SUCCESS).data.toString())
                    && dbRepository.saveUserName(userName = name)

        return if (dbResult) SignResults.SUCCESS else SignResults.ERROR.DB.WRITE_ERROR
    }
}