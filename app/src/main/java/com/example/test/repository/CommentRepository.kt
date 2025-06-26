package com.example.test.repository

import android.util.Log
import com.example.test.Comment
import com.example.test.repository.api.CommentDto
import com.example.test.repository.api.CommentService
import com.example.test.repository.api.RetrofitProvider
import com.example.test.repository.datebase.CommentDao
import com.example.test.repository.datebase.CommentEntity
import com.example.test.repository.datebase.DatabaseProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach


interface CommentRepository {
    suspend fun save(comment: Comment)
    suspend fun remove(comment: Comment)
    fun getComments(): Flow<List<Comment>>
}

class CommentRepositoryImpl : CommentRepository {

    private val commentDao: CommentDao = DatabaseProvider.database.commentDao()
    private val commentService: CommentService = RetrofitProvider.commentService

    override suspend fun save(comment: Comment) {
        Log.v("QWE","save: "+ comment.id+ "  " +comment.message)
        commentDao.insertAll(comment.toEntity())
    }

    private suspend fun save(comments: List<CommentDto>) {
        val commentsToSave = comments.map { comment -> comment.toEntity() }
        for(comment in commentsToSave){
            commentDao.insertAll(comment)
        }
    }

    override suspend fun remove(comment: Comment) {
        Log.v("QWE","remove: "+ comment.id+ "  " +comment.message)
        val commentToRemove = commentDao.findById(comment.id) ?: return
        commentDao.delete(commentToRemove)
    }

    override fun getComments(): Flow<List<Comment>> {
        return flow { emit(commentService.loadAll()) }
            .catch { emit(emptyList()) }
            .onEach { comments -> save(comments) }
            .flatMapLatest { getCommentsFromDB() }
    }

    private fun getCommentsFromDB() = commentDao.getAll()
        .map { comments -> comments.map { comment -> comment.toModel() } }

    private fun CommentEntity.toModel() = Comment(id = id, message = message)

    private fun CommentDto.toEntity() = CommentEntity(id = id.toString(), message = body)

    private fun Comment.toEntity() = CommentEntity(id = id, message = message)
}