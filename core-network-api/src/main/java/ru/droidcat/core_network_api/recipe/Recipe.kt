package ru.droidcat.core_network_api.recipe

abstract class Recipe {
    abstract val id: String
    abstract val databaseId: String
    abstract val name: String
    abstract val mainImage: String
}