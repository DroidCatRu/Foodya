package ru.droidcat.core_db_impl

import android.content.SharedPreferences
import androidx.room.RoomDatabase
import org.json.JSONArray
import ru.droidcat.core_db_api.DatabaseRepository
import ru.droidcat.core_db_api.UserFoodDatabase
import ru.droidcat.core_db_api.food.Food
import ru.droidcat.core_db_api.food.FoodCategory
import ru.droidcat.core_db_api.food.FoodGroup
import java.io.File
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val sharedPref: SharedPreferences,
    private val database: UserFoodDatabase
) : DatabaseRepository {

    private val USER_ID_KEY = "USER_ID_KEY"

    override suspend fun saveUserDatabaseId(databaseId: String): Boolean {
        return sharedPref
            .edit()
            .putString(USER_ID_KEY, databaseId)
            .commit()
    }

    override suspend fun getUserDatabaseId(): String? {
        return sharedPref.getString(USER_ID_KEY, null)
    }

    override suspend fun clearUserDatabaseId(): Boolean {
        return sharedPref
            .edit()
            .clear()
            .commit()
    }


    override suspend fun getAllFood(): List<Food> {
        return JsonHandler.getFoodList()
    }


    override suspend fun getFoodByCategory(foodCategory: FoodCategory): List<Food> {
        return JsonHandler.getFoodList().filter { food ->
            food.category == foodCategory
        }
    }


    override suspend fun getFoodByGroup(foodGroup: FoodGroup): List<Food> {
        return JsonHandler.getFoodList().filter { food ->
            food.group == foodGroup
        }
    }

    override suspend fun getFoodByName(foodName: String): Food? {
        return JsonHandler.getFoodList().find { food ->
            food.name == foodName
        }
    }

    object JsonHandler {
        private const val filePath = "src/main/food-db/Food.json"

        private var foodList: List<Food>? = null

        fun getFoodList(): List<Food> {
            if (foodList == null) {
                val file = File(filePath)

                val foodJsonArray =
                    JSONArray(file.bufferedReader().use { reader -> reader.readText() })
                val foodList = ArrayList<Food>()

                for (i in 0..foodJsonArray.length()) {
                    foodList.add(
                        Food(
                            null,
                            foodJsonArray.getJSONObject(i).getString("name"),
                            foodJsonArray.getJSONObject(i).getString("description"),
                            FoodGroup.valueOf(
                                foodJsonArray.getJSONObject(i).getString("food_group")
                                    .uppercase()
                                    .replace(" ", "_")
                            ),
                            FoodCategory.valueOf(
                                foodJsonArray.getJSONObject(i).getString("category").uppercase()
                                    .replace(" ", "_")
                            ),
                            null,
                            null
                        )
                    )

                }
            }
            return foodList!!
        }
    }


    override suspend fun storeUserFood(
        foodName: String,
        foodCreateTime: Long,
        foodExpirationTime: Long
    ) {
        database.userFoodDao.storeUserFood(foodName, foodCreateTime, foodExpirationTime)
    }

    override suspend fun removeUserFoodByName(foodName: String) {
        database.userFoodDao.removeUserFoodByName(foodName)
    }

    override suspend fun getUserFoodByName(foodName: String): Food {
        return database.userFoodDao.getUserFoodByName(foodName)
    }

    override suspend fun getAllUserFood(): List<Food> {
        return database.userFoodDao.getAllUserFood()
    }

    override suspend fun getUserExpireFood(daysToExpire: Int): List<Food> {
        val currentTimeStamp = System.currentTimeMillis() / 1000L
        return database.userFoodDao.getUserExpireFood(currentTimeStamp, daysToExpire)
    }
}