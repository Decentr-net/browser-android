package net.decentr.module_decentr_db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Profile")
data class ProfileEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "address")
    val address: String,
    @ColumnInfo(name = "avatar")
    val avatar: String?,
    @ColumnInfo(name = "isBanned")
    val isBanned: Boolean,
    @ColumnInfo(name = "bio")
    val bio: String?,
    @ColumnInfo(name = "birthDate")
    val birthDate: String?,
    @ColumnInfo(name = "createdAt")
    val createdAt: Long?,
    @ColumnInfo(name = "emails")
    val emails: List<String>?,
    @ColumnInfo(name = "firstName")
    val firstName: String,
    @ColumnInfo(name = "lastName")
    val lastName: String,
    @ColumnInfo(name = "gender")
    val gender: String?
)