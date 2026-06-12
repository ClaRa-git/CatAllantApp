package com.cfa.cda.catapp.data.repository

import android.content.Context
import com.cfa.cda.catapp.data.api.RetrofitInstance
import com.cfa.cda.catapp.data.db.FavoriteDatabase
import com.cfa.cda.catapp.data.db.FavoriteEntry
import com.cfa.cda.catapp.data.model.Breed
import com.cfa.cda.catapp.data.db.MyCat

class CatRepository(context: Context) {

    private val db = FavoriteDatabase(context)
    private var cachedBreeds: List<Breed>? = null

    suspend fun getAllBreeds(): List<Breed> {
        return cachedBreeds ?: RetrofitInstance.api.getBreeds().also { cachedBreeds = it }
    }

    suspend fun getBreedById(id: String): Breed? {
        return getAllBreeds().find { it.id == id }
    }

    fun getFavoriteEntry(breedId: String): FavoriteEntry? {
        return db.getEntry(breedId)
    }

    fun getAllFavorites(): List<FavoriteEntry> {
        return db.getAllFavorites()
    }

    fun toggleFavorite(breed: Breed, currentlyFavorite: Boolean) {
        val existing = db.getEntry(breed.id)
        val updated = (existing ?: FavoriteEntry(
            breedId = breed.id,
            name = breed.name,
            imageId = breed.referenceImageId,
            rating = 0f,
            isFavorite = false
        )).copy(isFavorite = !currentlyFavorite)
        db.upsert(updated)
    }

    fun setRating(breed: Breed, rating: Float) {
        val existing = db.getEntry(breed.id)
        val updated = (existing ?: FavoriteEntry(
            breedId = breed.id,
            name = breed.name,
            imageId = breed.referenceImageId,
            rating = 0f,
            isFavorite = false
        )).copy(rating = rating)
        db.upsert(updated)
    }

    fun getAllMyCats(): List<MyCat> = db.getAllMyCats()

    fun getMyCatById(id: Long): MyCat? = db.getMyCatById(id)

    fun saveMyCat(cat: MyCat): Long {
        return if (cat.id == 0L) {
            db.insertMyCat(cat)
        } else {
            db.updateMyCat(cat)
            cat.id
        }
    }

    fun deleteMyCat(id: Long) {
        db.deleteMyCat(id)
    }
}