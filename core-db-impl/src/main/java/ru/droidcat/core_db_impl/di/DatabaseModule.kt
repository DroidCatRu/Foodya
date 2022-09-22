package ru.droidcat.core_db_impl.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.droidcat.core_db_api.DatabaseRepository
import ru.droidcat.core_db_api.UserFoodDatabase
import ru.droidcat.core_db_impl.DatabaseRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object SharedPrefModule {

    private const val PREF_NAME = "ru.droidcat.foodya.pref"

    @Provides
    @Singleton
    fun providePrivateSharedPrefManager(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
}

@Module(includes = [DatabaseModule.BindsModule::class])
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideUserFoodDatabase(
        @ApplicationContext context: Context
    ): UserFoodDatabase {
        return Room.databaseBuilder(
            context,
            UserFoodDatabase::class.java, "user-food"
        ).build()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsModule {

        @Binds
        @Singleton
        fun bindDatabaseRepository(impl: DatabaseRepositoryImpl): DatabaseRepository
    }
}