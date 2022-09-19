package ru.droidcat.core_network_api

import ru.droidcat.core_network_api.hydration.HydrationInfo
import ru.droidcat.core_network_api.meal.Meal
import ru.droidcat.core_network_api.recipe.RecipeBasic
import ru.droidcat.core_network_api.recipe.RecipeFull
import ru.droidcat.core_network_api.users.Restriction
import ru.droidcat.core_network_api.users.UserData
import ru.droidcat.core_network_api.users.UserGender
import java.time.LocalDate

sealed class MutationResult {
    data class SUCCESS(val data: Any? = null) : MutationResult()
    sealed class ERROR : MutationResult() {
        sealed class AUTH : ERROR() {
            object USER_ALREADY_EXISTS : AUTH()
            object USER_ALREADY_SIGNED_IN : AUTH()
            object USER_NOT_SIGNED_IN : AUTH()
            object INVALID_CREDENTIALS : AUTH()
            object WEAK_PASSWORD : AUTH()
            object UNKNOWN : AUTH()
        }
        sealed class HTTP() : ERROR(){
            object BAD_REQUEST_400 : HTTP()
            object FORBIDDEN_403 : HTTP()
            object NOT_AUTHORIZED : HTTP()
            object UNKNOWN : HTTP()
        }
    }
}

interface NetworkRepository {

    suspend fun signUpUserEmailPassword(
        name: String,
        email: String,
        password: String
    ): MutationResult

    suspend fun signInUserEmailPassword(
        email: String,
        password: String
    ): MutationResult

    suspend fun removeUserProfile(databaseId: String): MutationResult

    suspend fun generateMealPlan(databaseId: String): MutationResult

    suspend fun getUserHydration(databaseId: String, date: String): HydrationInfo?

    suspend fun getUserMealPlan(databaseId: String): List<Meal>?

    suspend fun getPopularRecipes(): List<RecipeBasic>?

    suspend fun getRecipesByIngredients(ingredientsList: List<String>): List<RecipeBasic>?

    suspend fun getRecipeByID(id: String): RecipeFull?

    suspend fun getUsers(): List<UserData>?

    suspend fun removeUserMealPlan(databaseId: String): MutationResult

    suspend fun updateUserHydration(databaseId: String, date: String): MutationResult

    suspend fun updateMealPlan(databaseId: String, calories: Int): MutationResult

    suspend fun setProfileMacroGoals(
        databaseId: String,
        gender: UserGender,
        birthDate: String,
        height: Double,
        weight: Double
    ): MutationResult

    suspend fun getUserProfile(databaseId: String): UserData?

    suspend fun addUserRestrictions(databaseId: String, restrictions: List<String>) : MutationResult

    suspend fun getAllRestrictions() : List<Restriction>?

}