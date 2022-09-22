package ru.droidcat.core_notifications.di

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.droidcat.core_notifications.ExpiredFoodChecker
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NotificationsModule {

    @Provides
    @Singleton
    fun provideAlarmManager(
        @ApplicationContext context: Context
    ): AlarmManager {
        return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @Provides
    @Singleton
    fun provideCheckExpiredFoodPendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent {
        return Intent(context, ExpiredFoodChecker::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }
    }

}