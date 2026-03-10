package com.tech.pokedex.data.remote.model

import com.google.gson.annotations.SerializedName

data class PokemonDetailResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("abilities")
    val abilities: List<AbilitySlot>,
    @SerializedName("stats")
    val stats: List<StatSlot>,
    @SerializedName("types")
    val types: List<TypeSlot>
)

data class AbilitySlot(
    @SerializedName("ability") val ability: NamedResource,
    @SerializedName("is_hidden") val isHidden: Boolean,
    @SerializedName("slot") val slot: Int
)

data class StatSlot(
    @SerializedName("base_stat") val baseStat: Int,
    @SerializedName("effort") val effort: Int,
    @SerializedName("stat") val stat: NamedResource
)

data class TypeSlot(
    @SerializedName("slot") val slot: Int,
    @SerializedName("type") val type: NamedResource
)

data class NamedResource(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)