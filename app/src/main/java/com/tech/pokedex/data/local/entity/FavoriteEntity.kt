package com.tech.pokedex.data.local.entity

import androidx.room.Entity

@Entity(tableName = "favorite_pokemon", primaryKeys = ["id", "userId"])
data class FavoriteEntity(
    val id: Int,
    val userId: String,
    val name: String,
    val types: String,
    val timestamp: Long = System.currentTimeMillis()
)