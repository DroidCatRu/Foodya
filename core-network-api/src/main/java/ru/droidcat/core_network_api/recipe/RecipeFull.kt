package ru.droidcat.core_network_api.recipe

data class RecipeFull(
    override val id: String,
    override val databaseId: String,
    override val name: String,
    override val mainImage: String,
    override val servings: Int,
    override val weightInGrams: Float,
    val totalTime: String,
    val ingredientLines: List<String>,
    val instructions: List<String>
) : Recipe()
