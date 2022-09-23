package ru.droidcat.feature_main_screen_api.usecase

import ru.droidcat.core_db_api.DatabaseRepository
import ru.droidcat.core_network_api.NetworkRepository
import ru.droidcat.core_network_api.hydration.HydrationAction
import java.util.*
import javax.inject.Inject

class AddWaterUseCase @Inject constructor(
    private val dbRepository: DatabaseRepository,
    private val networkRepository: NetworkRepository
) {

    suspend operator fun invoke() {
        val dbId = dbRepository.getUserDatabaseId()

        if (dbId.isNullOrBlank()) {
            return
        }

        val calendar = Calendar.getInstance()
        val date = "${calendar.get(Calendar.YEAR)}-" +
                "${calendar.get(Calendar.MONTH) + 1}-" +
                "${calendar.get(Calendar.DAY_OF_MONTH)}"

        networkRepository.updateUserHydration(
            databaseId = dbId,
            date = date,
            action = HydrationAction.INCREASE
        )
    }
}