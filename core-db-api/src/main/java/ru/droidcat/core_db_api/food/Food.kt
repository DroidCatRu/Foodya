package ru.droidcat.core_db_api.food

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName="food")
data class Food(
    @PrimaryKey(autoGenerate = true)
    val uid: Int? = null,

    @ColumnInfo(name = "name")
    val name: String,

//    @Ignore
    val description: String,

//    @Ignore
    val group: FoodGroup,

//    @Ignore
    val category: FoodCategory,

    @ColumnInfo(name = "create_time")
    val createTime: Long? = null,

    @ColumnInfo(name = "expiration_time")
    val expirationTime: Long? = null
) {

}