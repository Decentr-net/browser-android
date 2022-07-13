package net.decentr.module_decentr_db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PDV")
data class PDVEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "address")
    val address: String?,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "domain")
    val domain: String? = null,
    @ColumnInfo(name = "engine")
    val engine: String? = null,
    @ColumnInfo(name = "query")
    val query: String? = null,
    @ColumnInfo(name = "timestamp")
    val timestamp: String? = null,
    @ColumnInfo(name = "avatar")
    val avatar: String? = null,
    @ColumnInfo(name = "bio")
    val bio: String? = null,
    @ColumnInfo(name = "birthday")
    val birthday: String? = null,
    @ColumnInfo(name = "emails")
    val emails: List<String>? = null,
    @ColumnInfo(name = "firstName")
    val firstName: String? = null,
    @ColumnInfo(name = "lastName")
    val lastName: String? = null,
    @ColumnInfo(name = "gender")
    val gender: String? = null
)