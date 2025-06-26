package com.example.test.repository

import com.example.test.Comment
import com.example.test.repository.api.CommentDto
import com.example.test.repository.datebase.CommentDao
import com.example.test.repository.datebase.CommentEntity
import com.example.test.repository.datebase.DatabaseProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


interface CommentRepository {
    suspend fun save(comment: Comment)
    suspend fun remove(comment: Comment)
    fun getComments(): Flow<List<Comment>>
}

class CommentRepositoryImpl(private val commentDao: CommentDao = DatabaseProvider.database.commentDao()) : CommentRepository {

    override suspend fun save(comment: Comment) {
        commentDao.insertAll(comment.toEntity())
    }

    private suspend fun save(comments: List<CommentDto>) {
        val commentsToSave = comments.map { comment -> comment.toEntity() }
        for(comment in commentsToSave){
            commentDao.insertAll(comment)
        }
    }

    override suspend fun remove(comment: Comment) {
        commentDao.delete(CommentEntity(comment.id, comment.message))
    }

    override fun getComments(): Flow<List<Comment>> {
        return getCommentsFromDB()
    }

    private fun getCommentsFromDB() = commentDao.getAll()
        .map { comments -> comments.map { comment -> comment.toModel() } }

    private fun CommentEntity.toModel() = Comment(id = id, message = message)

    private fun CommentDto.toEntity() = CommentEntity(id = id.toString(), message = body)

    private fun Comment.toEntity() = CommentEntity(id = id, message = message)
}