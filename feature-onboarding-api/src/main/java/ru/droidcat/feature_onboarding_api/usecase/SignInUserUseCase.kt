package ru.droidcat.feature_onboarding_api.usecase

import ru.droidcat.core_db_api.DatabaseRepository
import ru.droidcat.core_network_api.MutationResult
import ru.droidcat.core_network_api.NetworkRepository

class SignInUserUseCase(
    private val networkRepository: NetworkRepository,
    private val dbRepository: DatabaseRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): SignInResult {
        val networkResult = networkRepository.signInUserEmailPassword(email, password)
        when (networkResult) {
            is MutationResult.SUCCESS -> {
                dbRepository.saveUserDatabaseId(networkResult.data.toString())
            }
            else -> {

            }
        }
    }
}

sealed class SignInResult {
    object SUCCESS : SignInResult()
    sealed class ERROR : SignInResult() {
            object USER_ALREADY_EXISTS : ERROR()
            object USER_ALREADY_SIGNED_IN : ERROR()
            object USER_NOT_SIGNED_IN : ERROR()
            object INVALID_CREDENTIALS : ERROR()
            object WEAK_PASSWORD : ERROR()
            object UNKNOWN : ERROR()
    }
}