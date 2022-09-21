package ru.droidcat.feature_onboarding_api

sealed class SignResults {
    object SUCCESS : SignResults()
    sealed class ERROR : SignResults() {
        object USER_ALREADY_EXISTS : ERROR()
        object USER_ALREADY_SIGNED_IN : ERROR()
        object USER_NOT_SIGNED_IN : ERROR()
        object INVALID_CREDENTIALS : ERROR()
        object WEAK_PASSWORD : ERROR()
        object BAD_REQUEST_400 : ERROR()
        object FORBIDDEN_403 : ERROR()
        object NOT_AUTHORIZED : ERROR()
        object UNKNOWN : ERROR()

        sealed class DB : SignResults(){
            object WRITE_ERROR : DB()
        }
    }
}