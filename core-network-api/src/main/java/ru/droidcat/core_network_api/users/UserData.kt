package ru.droidcat.core_network_api.users

data class UserData(
    val userId: String,
    val databaseId: String,
    val name: String,
    val email: String,
    val birthdate: String?,
    val gender: UserGender?,
    val activityLevel: String?,
    val weight: Int?,
    val height: Double?,
    val goalsOn: Boolean?,
    val dailyCaloricIntakeGoal: Int?,
    val restrictions: List<Restriction>?
)
