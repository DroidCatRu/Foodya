package ru.droidcat.core_notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.droidcat.core_db_api.DatabaseRepository
import ru.droidcat.core_utils.IoScope
import java.util.*
import javax.inject.Inject

class FoodNotificationManager @Inject constructor(
    private val alarmManager: AlarmManager,
    private val pendingIntent: PendingIntent
) {

    fun setAlarm() {

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 9)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

    }

    fun stopAlarm() {

        alarmManager.cancel(pendingIntent)

    }

}

class ExpiredFoodChecker @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    @IoScope private val scope: CoroutineScope
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        scope.launch {
            val expiredFoodList = databaseRepository.getUserExpireFood(1)
            if (expiredFoodList != null) {
                //ToDo Показать уведомление
            }
        }

    }
}