package com.cfa.cda.catapp.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class FavoriteEntry(
    val breedId: String,
    val name: String,
    val imageId: String?,
    val rating: Float,
    val isFavorite: Boolean
)

data class MyCat(
    val id: Long = 0,
    val name: String,
    val breedId: String?,      // null si race personnalisee
    val customBreedName: String?, // utilise si breedId == null
    val photoUri: String?,     // chemin vers la photo locale
    val age: Int?,             // en mois ou annees
    val weight: Float?,        // en kg
    val notes: String?
)

class FavoriteDatabase(context: Context) :
    SQLiteOpenHelper(context, "catapi.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE favorites (" +
                    "breedId TEXT PRIMARY KEY, " +
                    "name TEXT, " +
                    "imageId TEXT, " +
                    "rating REAL DEFAULT 0, " +
                    "isFavorite INTEGER DEFAULT 0" +
                    ");"
        )
        createMyCatsTable(db)
    }

    private fun createMyCatsTable(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE my_cats (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "breedId TEXT, " +
                    "customBreedName TEXT, " +
                    "photoUri TEXT, " +
                    "age INTEGER, " +
                    "weight REAL, " +
                    "notes TEXT" +
                    ");"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            createMyCatsTable(db)
        }
    }

    // --- Favorites (inchange) ---

    @android.annotation.SuppressLint("Range")
    fun getEntry(breedId: String): FavoriteEntry? {
        readableDatabase.rawQuery(
            "SELECT * FROM favorites WHERE breedId = ?", arrayOf(breedId)
        ).use { cursor ->
            if (cursor.moveToFirst()) {
                return FavoriteEntry(
                    breedId = cursor.getString(cursor.getColumnIndex("breedId")),
                    name = cursor.getString(cursor.getColumnIndex("name")),
                    imageId = cursor.getString(cursor.getColumnIndex("imageId")),
                    rating = cursor.getFloat(cursor.getColumnIndex("rating")),
                    isFavorite = cursor.getInt(cursor.getColumnIndex("isFavorite")) == 1
                )
            }
        }
        return null
    }

    @android.annotation.SuppressLint("Range")
    fun getAllFavorites(): List<FavoriteEntry> {
        val list = mutableListOf<FavoriteEntry>()
        readableDatabase.rawQuery(
            "SELECT * FROM favorites WHERE isFavorite = 1", null
        ).use { cursor ->
            while (cursor.moveToNext()) {
                list.add(
                    FavoriteEntry(
                        breedId = cursor.getString(cursor.getColumnIndex("breedId")),
                        name = cursor.getString(cursor.getColumnIndex("name")),
                        imageId = cursor.getString(cursor.getColumnIndex("imageId")),
                        rating = cursor.getFloat(cursor.getColumnIndex("rating")),
                        isFavorite = true
                    )
                )
            }
        }
        return list
    }

    fun upsert(entry: FavoriteEntry) {
        val values = ContentValues().apply {
            put("breedId", entry.breedId)
            put("name", entry.name)
            put("imageId", entry.imageId)
            put("rating", entry.rating)
            put("isFavorite", if (entry.isFavorite) 1 else 0)
        }
        writableDatabase.insertWithOnConflict(
            "favorites", null, values, SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    // --- My Cats ---

    @android.annotation.SuppressLint("Range")
    fun getAllMyCats(): List<MyCat> {
        val list = mutableListOf<MyCat>()
        readableDatabase.rawQuery("SELECT * FROM my_cats ORDER BY id DESC", null).use { cursor ->
            while (cursor.moveToNext()) {
                list.add(cursorToMyCat(cursor))
            }
        }
        return list
    }

    @android.annotation.SuppressLint("Range")
    fun getMyCatById(id: Long): MyCat? {
        readableDatabase.rawQuery("SELECT * FROM my_cats WHERE id = ?", arrayOf(id.toString())).use { cursor ->
            if (cursor.moveToFirst()) {
                return cursorToMyCat(cursor)
            }
        }
        return null
    }

    private fun cursorToMyCat(cursor: android.database.Cursor): MyCat {
        return MyCat(
            id = cursor.getLong(cursor.getColumnIndex("id")),
            name = cursor.getString(cursor.getColumnIndex("name")),
            breedId = cursor.getString(cursor.getColumnIndex("breedId")),
            customBreedName = cursor.getString(cursor.getColumnIndex("customBreedName")),
            photoUri = cursor.getString(cursor.getColumnIndex("photoUri")),
            age = if (cursor.isNull(cursor.getColumnIndex("age"))) null else cursor.getInt(cursor.getColumnIndex("age")),
            weight = if (cursor.isNull(cursor.getColumnIndex("weight"))) null else cursor.getFloat(cursor.getColumnIndex("weight")),
            notes = cursor.getString(cursor.getColumnIndex("notes"))
        )
    }

    fun insertMyCat(cat: MyCat): Long {
        val values = ContentValues().apply {
            put("name", cat.name)
            put("breedId", cat.breedId)
            put("customBreedName", cat.customBreedName)
            put("photoUri", cat.photoUri)
            cat.age?.let { put("age", it) } ?: putNull("age")
            cat.weight?.let { put("weight", it) } ?: putNull("weight")
            put("notes", cat.notes)
        }
        return writableDatabase.insert("my_cats", null, values)
    }

    fun updateMyCat(cat: MyCat) {
        val values = ContentValues().apply {
            put("name", cat.name)
            put("breedId", cat.breedId)
            put("customBreedName", cat.customBreedName)
            put("photoUri", cat.photoUri)
            cat.age?.let { put("age", it) } ?: putNull("age")
            cat.weight?.let { put("weight", it) } ?: putNull("weight")
            put("notes", cat.notes)
        }
        writableDatabase.update("my_cats", values, "id = ?", arrayOf(cat.id.toString()))
    }

    fun deleteMyCat(id: Long) {
        writableDatabase.delete("my_cats", "id = ?", arrayOf(id.toString()))
    }
}