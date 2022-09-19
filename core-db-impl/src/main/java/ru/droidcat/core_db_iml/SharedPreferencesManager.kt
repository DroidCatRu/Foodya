package ru.droidcat.core_db_iml

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {

    private val PREF_NAME = "com.example.app.PREF_NAME"
    private val KEY_VALUE = "com.example.app.KEY_VALUE"

    private var sInstance: SharedPreferencesManager? = null
    private var mPref: SharedPreferences? = null

    private fun PreferencesManager(context: Context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    @Synchronized
    fun initializeInstance(context: Context) {
        if (sInstance == null) {
            sInstance = SharedPreferencesManager(context)
        }
    }

    @Synchronized
    fun getInstance(): SharedPreferencesManager? {
        if (sInstance == null) {
            throw IllegalStateException(
                SharedPreferencesManager::class.java.getSimpleName() +
                        " is not initialized, call initializeInstance(..) method first."
            )
        }
        return sInstance
    }

    fun setValue(value: Long) {
        mPref!!.edit()
            .putLong(KEY_VALUE, value)
            .commit()
    }

    fun getValue(): Long {
        return mPref!!.getLong(KEY_VALUE, 0)
    }

    fun remove(key: String?) {
        mPref!!.edit()
            .remove(key)
            .commit()
    }

    fun clear(): Boolean {
        return mPref!!.edit()
            .clear()
            .commit()
    }

}