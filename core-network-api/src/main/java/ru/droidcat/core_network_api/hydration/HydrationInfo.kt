package ru.droidcat.core_network_api.hydration

data class HydrationInfo(
    val isToday: Boolean,
    val quantity: Int,
    val date: String,
    val goal: Int
)
