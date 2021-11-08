package com.example.calculator.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.calculator.data.db.history.HistoryItemDao
import com.example.calculator.data.db.history.HistoryItemEntity
import com.example.calculator.data.db.typeConverter.LocalTimeConverter


@Database(
    entities = [HistoryItemEntity::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(LocalTimeConverter::class)

abstract class MainDatabase: RoomDatabase() {

    abstract val historyItemDao: HistoryItemDao

    companion object {
        fun create(context: Context): MainDatabase {
            return Room.databaseBuilder(context, MainDatabase::class.java, "main_database")
                .addMigrations(MigrationFromTo())

                .build()
        }
    }
}
