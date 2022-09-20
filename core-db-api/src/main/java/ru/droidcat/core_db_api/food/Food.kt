package ru.droidcat.core_db_api.food

data class Food(
    val name: String,
    val description: String,
    val group: FoodGroup,
    val category: FoodCategory
)
