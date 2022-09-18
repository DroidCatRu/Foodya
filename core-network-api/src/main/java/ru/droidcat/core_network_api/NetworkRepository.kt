package ru.droidcat.core_network_api

import ru.droidcat.core_network_api.hydration.HydrationInfo
import ru.droidcat.core_network_api.meal.Meal
import ru.droidcat.core_network_api.recipe.RecipeBasic
import ru.droidcat.core_network_api.recipe.RecipeFull
import ru.droidcat.core_network_api.users.UserData
import ru.droidcat.core_network_api.users.UserGender
import java.time.LocalDate

sealed class MutationResult {
    object SUCCESS : MutationResult()
    sealed class ERROR : MutationResult() {
        sealed class AUTH : ERROR() {
            object USER_ALREADY_EXISTS : AUTH()
            object USER_ALREADY_SIGNED_IN : AUTH()
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

    suspend fun removeUserProfile(): MutationResult

    suspend fun generateMealPlan(): MutationResult

    suspend fun getUserHydration(date: String): HydrationInfo

    suspend fun getUserMealPlan(): List<Meal>

    suspend fun getPopularRecipes(): List<RecipeBasic>

    suspend fun getRecipesByIngredients(): List<RecipeBasic>

    suspend fun getRecipeByID(id: String): RecipeFull

    suspend fun getUsers(): List<UserData>

    suspend fun removeUserMealPlan(): MutationResult

    suspend fun updateUserHydration(date: String): MutationResult

    suspend fun updateMealPlan(calories: Int): MutationResult

    suspend fun setProfileMacroGoals(
        gender: UserGender,
        birthDate: String,
        height: Int,
        weight: Int
    ): MutationResult

    suspend fun getUserProfile(): UserData?
}