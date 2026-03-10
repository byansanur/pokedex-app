package com.tech.pokedex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_list")
data class PokemonEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val url: String
)