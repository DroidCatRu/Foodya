package ru.droidcat.core_network_api

import ru.droidcat.core_network_api.users.UserData
import ru.droidcat.core_network_api.users.UserGender
import java.time.LocalDate

sealed class MutationResult {
    object SUCCESS : MutationResult()
    sealed class ERROR : MutationResult() {
        sealed class AUTH : ERROR() {
            object USER_ALREADY_EXISTS : AUTH()
            object USER_ALREADY_SIGNED_IN : AUTH()
            object INVALID_CREDENTIALS : AUTH()
            object WEAK_PASSWORD : AUTH()
            object UNKNOWN : AUTH()
        }
    }
}

interface NetworkRepository {

    suspend fun signUpUserEmailPassword(
        name: String,
        email: String,
        password: String
    ): MutationResult

    suspend fun signInUserEmailPassword(
        email: String,
        password: String
    ): MutationResult

    suspend fun setUserParameters(
        weight: Int,
        height: Int,
        birthDate: LocalDate,
        gender: UserGender
    ): MutationResult

    suspend fun getUserProfile(): UserData?
}