package com.example.test.repository.datebase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comment")
data class CommentEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = "message") val message: String,
    //@ColumnInfo(name = "crate_data")
    // val createData: LocalDataTime
)