package net.decentr.module_decentr_db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        if (database.isOpen) {
            database.execSQL("ALTER TABLE PDV ADD COLUMN address TEXT DEFAULT NULL")
        }
    }
}

val ALL_MIGRATIONS = arrayOf<Migration>(
    MIGRATION_1_2
)