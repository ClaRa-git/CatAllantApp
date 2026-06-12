package com.cfa.cda.catapp.data.model

import com.squareup.moshi.Json

data class Breed(
    val id: String,
    val name: String,
    val origin: String?,
    val description: String?,
    val weight: Weight?,
    @param:Json(name = "wikipedia_url") val wikipediaUrl: String?,
    @param:Json(name = "reference_image_id") val referenceImageId: String?,
    @param:Json(name = "affection_level") val affectionLevel: Int? = null,
    @param:Json(name = "energy_level") val energyLevel: Int? = null
)

data class Weight(
    val imperial: String?,
    val metric: String?
)