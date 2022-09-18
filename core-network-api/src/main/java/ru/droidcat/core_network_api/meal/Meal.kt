package ru.droidcat.core_network_api.meal

import ru.droidcat.core_network_api.recipe.RecipeBasic

data class Meal(
    val meal: MealType,
    val recipe: RecipeBasic
)

enum class MealType {
    BREAKFAST, SNACK, LUNCH, DINNER
}
