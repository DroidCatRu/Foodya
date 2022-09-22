package ru.droidcat.core_db_api

import androidx.room.Dao
import ru.droidcat.core_db_api.food.Food
import ru.droidcat.core_db_api.food.FoodCategory
import ru.droidcat.core_db_api.food.FoodGroup

@Dao
interface DatabaseRepository {

    //User's databaseId

    suspend fun saveUserDatabaseId(databaseId: String): Boolean

    suspend fun getUserDatabaseId(): String?

    suspend fun clearUserDatabaseId(): Boolean


    //Local food repository (Food.json file)

    suspend fun getAllFood(): List<Food>

    suspend fun getFoodByCategory(foodCategory: FoodCategory): List<Food>

    suspend fun getFoodByGroup(foodGroup: FoodGroup): List<Food>

    suspend fun getFoodByName(foodName: String): Food?


    //User's food

    suspend fun storeUserFood(foodName: String, foodCreateTime: Long, foodExpirationTime: Long)

    suspend fun removeUserFoodByName(foodName: String)

    suspend fun getUserFoodByName(foodName: String): Food?

    suspend fun getAllUserFood(): List<Food>?

    suspend fun getUserExpireFood(daysToExpire: Int): List<Food>?

}
