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

        if (networkResult is MutationResult.ERROR) {
            return SignInResult.ERROR.UNKNOWN
        }

        val dbResult =
            dbRepository.saveUserDatabaseId((networkResult as MutationResult.SUCCESS).data.toString())

        if (!dbResult) {
            return SignInResult.ERROR.DB_WRITE_ERROR
        }

        return SignInResult.SUCCESS
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
        object DB_WRITE_ERROR : ERROR()
    }
}