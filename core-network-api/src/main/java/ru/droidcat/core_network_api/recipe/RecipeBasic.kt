package ru.droidcat.core_network_api.recipe

data class RecipeBasic(
    override val id: String,
    override val databaseId: String,
    override val name: String,
    override val mainImage: String
) : Recipe()
