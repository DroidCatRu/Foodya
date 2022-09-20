package ru.droidcat.core_db_api

import ru.droidcat.core_db_api.food.Food
import ru.droidcat.core_db_api.food.FoodCategory
import ru.droidcat.core_db_api.food.FoodGroup

interface DatabaseRepository {

    suspend fun saveUserDatabaseId(databaseId: String)

    suspend fun getUserDatabaseId() : String?

    suspend fun clearLocalStorage()


    suspend fun getAllFoods() : List<Food>

    suspend fun getFoodByCategory(foodCategory: FoodCategory): List<Food>

    suspend fun getFoodByGroup(foodGroup: FoodGroup): List<Food>

    suspend fun getFoodByName(foodName: String): Food?

}