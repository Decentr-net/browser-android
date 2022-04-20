package net.decentr.module_decentr.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.decentr.module_decentr.local.database.converter.DateConverter
import net.decentr.module_decentr.local.database.converter.ListOfStringsConverter
import net.decentr.module_decentr.local.database.dao.PDVDao
import net.decentr.module_decentr.local.database.dao.ProfileDao
import net.decentr.module_decentr.local.database.entities.PDVEntity
import net.decentr.module_decentr.local.database.entities.ProfileEntity
import javax.inject.Singleton

@Database(
        version = 2,
        entities = [
            ProfileEntity::class,
            PDVEntity::class
        ],
        exportSchema = true
)
@TypeConverters(value = [DateConverter::class, ListOfStringsConverter::class])
@Singleton
abstract class ProjectDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao
    abstract fun pdvDao(): PDVDao

    companion object {

        private const val DB_NAME = "decentr.db"

        @Volatile
        private var INSTANCE: ProjectDatabase? = null

        fun getInstance(context: Context): ProjectDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }
        }

        private fun buildDatabase(context: Context): ProjectDatabase {
            return Room.databaseBuilder(context, ProjectDatabase::class.java, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .addMigrations(*ALL_MIGRATIONS)
                .build()
        }
    }
}