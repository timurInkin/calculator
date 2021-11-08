package com.example.calculator.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MigrationFromTo : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "alter table history_item_entity add column createdAt TEXT DEFAULT \"${
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            }\" not null"
        )
    }
}
