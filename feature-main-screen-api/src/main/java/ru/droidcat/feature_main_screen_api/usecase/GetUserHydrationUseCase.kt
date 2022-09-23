package ru.droidcat.feature_main_screen_api.usecase

import android.util.Log
import ru.droidcat.core_db_api.DatabaseRepository
import ru.droidcat.core_network_api.NetworkRepository
import java.util.Calendar
import javax.inject.Inject

class GetUserHydrationUseCase @Inject constructor(
    private val dbRepository: DatabaseRepository,
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke(): Hydration? {
        val dbId = dbRepository.getUserDatabaseId()

        if (dbId.isNullOrBlank()) {
            return null
        }

        val calendar = Calendar.getInstance()
        val date = "${calendar.get(Calendar.YEAR)}-" +
                "${calendar.get(Calendar.MONTH) + 1}-" +
                "${calendar.get(Calendar.DAY_OF_MONTH)}"

        val result = networkRepository.getUserHydration(dbId, date) ?: return null

        return Hydration(result.quantity * 0.25f, result.goal * 0.25f)
    }
}

data class Hydration(
    val intake: Float = 0f,
    val goal: Float = 0f
)