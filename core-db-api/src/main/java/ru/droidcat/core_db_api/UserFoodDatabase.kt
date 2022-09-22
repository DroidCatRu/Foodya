package ru.droidcat.core_db_api

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.droidcat.core_db_api.food.Food

@Database(entities = [Food::class], version = 1)
abstract class UserFoodDatabase : RoomDatabase(){
    abstract val userFoodDao: UserFoodDao
}