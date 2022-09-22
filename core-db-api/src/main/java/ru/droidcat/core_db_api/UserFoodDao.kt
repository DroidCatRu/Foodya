package ru.droidcat.core_db_api

import androidx.room.Dao
import androidx.room.Query
import ru.droidcat.core_db_api.food.Food

@Dao
interface UserFoodDao {

    @Query("INSERT INTO food (name, create_time, expiration_time) VALUES (:foodName, :foodCreateTime, :foodExpirationTime) ")
    suspend fun storeUserFood(foodName: String, foodCreateTime: Long, foodExpirationTime: Long)

    @Query("DELETE FROM food WHERE name = :foodName")
    suspend fun removeUserFoodByName(foodName: String)

    @Query("SELECT * FROM food WHERE name = :foodName")
    suspend fun getUserFoodByName(foodName: String): Food?

    @Query("SELECT * FROM food")
    suspend fun getAllUserFood(): List<Food>?

    @Query("SELECT * FROM food WHERE expiration_time - :currentTimestamp < 86400 * :daysToExpire")
    suspend fun getUserExpireFood(currentTimestamp: Long, daysToExpire: Int): List<Food>?

}