package ru.droidcat.feature_onboarding_api

import ru.droidcat.core_network_api.MutationResult

sealed class SignResults {
    object SUCCESS : SignResults()
    sealed class ERROR : SignResults() {
        object USER_ALREADY_EXISTS : ERROR()
        object USER_ALREADY_SIGNED_IN : ERROR()
        object USER_NOT_SIGNED_IN : ERROR()
        object INVALID_CREDENTIALS : ERROR()
        object WEAK_PASSWORD : ERROR()
        object BAD_REQUEST_400 : SignResults.ERROR()
        object FORBIDDEN_403 : SignResults.ERROR()
        object NOT_AUTHORIZED : SignResults.ERROR()
        object UNKNOWN : ERROR()
    }
    sealed class DB : SignResults(){
        object WRITE_ERROR : ERROR()
    }
}