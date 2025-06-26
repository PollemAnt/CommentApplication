package com.example.test

import com.example.test.repository.CommentRepository
import com.example.test.repository.datebase.CommentDao
import com.example.test.repository.datebase.CommentEntity
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test


class CommentRepositoryTest {

    private val repository = mockk<CommentRepository>()
    private val commentDao = mockk<CommentDao>(relaxed = true)

    @Test
    fun `getComments returns comments from DAO`() = runTest {
        // Given
        val mockCommentEntities = listOf(
            CommentEntity("1", "DAO Comment 1"),
            CommentEntity("2", "DAO Comment 2")
        )
        val expectedComments = mockCommentEntities.map { Comment(it.id, it.message) }
        every { commentDao.getAll() } returns flowOf(mockCommentEntities)

        // When
        val comments = repository.getComments().first()

        // Then
        assertEquals(expectedComments, comments)
        verify(exactly = 1) { commentDao.getAll() }
        confirmVerified(commentDao)
    }

    @Test
    fun `saveComment calls DAO insertComment`() = runTest {
        // Given
        val commentToSave = Comment("3", "New comment to save")

        // When
        repository.save(commentToSave)

        // Then
          coVerify(exactly = 1) {
              commentDao.insertAll(match {
                 it.id == commentToSave.id && it.message == commentToSave.message
             })
          }
        confirmVerified(commentDao)
    }


    @Test
    fun `removeComment calls DAO deleteComment`() = runTest {
        // Given
        val commentToRemove = Comment("id_to_delete", "Comment to delete")

        // When
        repository.remove(commentToRemove)

        // Then
        coVerify(exactly = 1) {
            commentDao.delete(match {
                it.id == commentToRemove.id && it.message == commentToRemove.message
            })
        }
        confirmVerified(commentDao)
    }
}