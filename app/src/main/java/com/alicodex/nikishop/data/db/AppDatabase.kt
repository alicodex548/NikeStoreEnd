package com.alicodex.nikishop.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alicodex.nikishop.data.Product
import com.alicodex.nikishop.data.repo.source.ProductLocalDataSource

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductLocalDataSource
}