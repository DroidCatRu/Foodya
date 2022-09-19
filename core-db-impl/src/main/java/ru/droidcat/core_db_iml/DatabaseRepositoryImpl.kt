package ru.droidcat.core_db_iml

import android.content.SharedPreferences
import ru.droidcat.core_db_api.DatabaseRepository
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val sharedPref: SharedPreferences
    ) : DatabaseRepository {

    private val USER_ID_KEY = "USER_ID_KEY"

    override suspend fun saveUserDatabaseId(databaseId: String) {
        sharedPref
            .edit()
            .putString(USER_ID_KEY, databaseId)
            .apply()
    }

    override suspend fun getUserDatabaseId(): String? {
        return sharedPref.getString(USER_ID_KEY, null)
    }

    override suspend fun clearLocalStorage() {
        sharedPref
            .edit()
            .clear()
            .apply()
    }
}