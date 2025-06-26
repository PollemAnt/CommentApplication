package com.example.test

import com.example.test.repository.CommentRepository
import com.example.test.viewModel.CommentsViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CommentsViewModelTest {

    private val commentsState = MutableStateFlow(emptyList<Comment>())
    private val repository = mockk<CommentRepository>()
    private lateinit var viewModel: CommentsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        every { repository.getComments() } returns commentsState
        coEvery { repository.save(any()) } answers { call ->
            val comment = call.invocation.args[0] as Comment
            commentsState.value += comment
        }
        coEvery { repository.remove(any()) } answers { call ->
            val comment = call.invocation.args[0] as Comment
            commentsState.value -= comment
        }
        viewModel = CommentsViewModel(repository)
    }

    @Test
    fun `screen should start without any comments`() = runTest {
        assertEquals(emptyList<Comment>(), viewModel.state.value.comments)
    }

    @Test
    fun `added comment is saved to UI comment list`() {
        val commentToAdd = "Added Comment"

        viewModel.addComment(commentToAdd)

        val uiState = viewModel.state.value.comments
        assertEquals(1, uiState.size)
        assertEquals(commentToAdd, uiState[0].message)
    }

    @Test
    fun `added comment is saved in repository`() = runTest {
        val commentToAdd = "Added Comment"

        viewModel.addComment(commentToAdd)

        coVerify(exactly = 1) { repository.save(match { comment -> comment.message == commentToAdd }) }
    }

    @Test
    fun `removed comment is removed from UI comment list`() = runTest {
        val commentToRemove = Comment(
            id = "id",
            message = "Removed Comment"
        )
        commentsState.value += commentToRemove
        val idCommentToRemove = commentToRemove.id

        viewModel.removeComment(idCommentToRemove)

        coVerify(exactly = 1) { repository.remove(commentToRemove) }
        assertTrue(commentToRemove !in viewModel.state.value.comments)
        assertEquals(0, viewModel.state.value.comments.size)
    }
}