package ru.droidcat.core_db_api

interface DatabaseRepository {

    suspend fun saveUserDatabaseId(databaseId: String)

    suspend fun getUserDatabaseId() : String?

    suspend fun clearLocalStorage()

}