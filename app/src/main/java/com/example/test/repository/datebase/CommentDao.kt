package com.example.test.repository.datebase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {

    @Query("SELECT * FROM comment WHERE id = :id LIMIT 1")
    suspend fun findById(id: String): CommentEntity?

    @Query("SELECT * FROM comment")
    fun getAll(): Flow<List<CommentEntity>>

    @Query("SELECT * FROM comment WHERE id IN (:ids)")
    suspend fun loadAllByIds(ids: IntArray): List<CommentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg comments: CommentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(comments: List<CommentEntity>)

    @Delete
    suspend fun delete(comment: CommentEntity)

}
