package com.tech.pokedex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_pokemon")
data class FavoriteEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val types: String,
    val timestamp: Long = System.currentTimeMillis()
)