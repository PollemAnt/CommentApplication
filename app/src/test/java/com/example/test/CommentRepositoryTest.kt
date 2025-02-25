package com.example.test

import com.example.test.repository.CommentRepository
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions

class CommentRepositoryTest {

    private val repository = mockk<CommentRepository>()


    @Test
    fun `save should add comment to DB`() = runTest {
        //given
        val commentToAdd = Comment(id = "id", message = "Add this Comment")

        //when


        //then
    }


    @Test
    fun `get comment test`() {
        repository.getComments()
        //given

        //when

        //then

    }

    @Test
    fun `MOCCK addComment added comment`() = runTest {
        //given
        val comment = Comment(id = "id-com-test", message = "commentMessage")

        //when
        repository.save(comment)

        //then
        val list = getListFromFlow(repository.getComments())
        Assertions.assertEquals(1, list.size)
        Assertions.assertEquals("commentMessage", list[0].message)
    }

    private fun Comment.toModel() = Comment(id = id, message = message)

    suspend fun getListFromFlow(flow: Flow<List<Comment>>): List<Comment> {
        var result = emptyList<Comment>()
        flow.collect { list ->
            result = list
        }
        return result
    }
}