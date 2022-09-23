package ru.droidcat.feature_main_screen_api.usecase

import ru.droidcat.core_network_api.NetworkRepository
import ru.droidcat.core_network_api.recipe.RecipeBasic
import javax.inject.Inject

class GetRecommendedRecipesUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {

    suspend operator fun invoke(): List<Recipe>? {
        return networkRepository.getPopularRecipes()?.map {
            Recipe(
                id = it.id,
                name = it.name,
                servings = it.servings,
                weight = it.weightInGrams,
                imageUrl = it.mainImage
            )
        }
    }
}

data class Recipe(
    val id: String,
    val name: String,
    val servings: Int,
    val weight: Float,
    val imageUrl: String
)