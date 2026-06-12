package com.cfa.cda.catapp.data.api

import com.cfa.cda.catapp.data.model.Breed
import retrofit2.http.GET

interface CatApiService {
    @GET("breeds")
    suspend fun getBreeds(): List<Breed>
}