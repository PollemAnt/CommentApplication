package com.example.test.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.Comment
import com.example.test.repository.CommentRepository
import com.example.test.repository.CommentRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


data class CommentsState(
    val comments: List<Comment> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class CommentsViewModel(private val repository: CommentRepository) : ViewModel() {
    constructor() : this(CommentRepositoryImpl())

    var id = 0
    private val query = MutableStateFlow("")
    val state = MutableStateFlow(CommentsState())
    val uiState = combine(
        state,
        query.debounce(300).onStart { emit("") })
    { state, query ->
        state.copy(
            comments = state.comments
                .filter { comment -> comment.message.contains(query) }
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, state.value)

    init {
        viewModelScope.launch {
            repository.getComments()
                .catch { state.value = state.value.copy(errorMessage = "Failed to load comments") }
                .collectLatest { comments -> state.value = state.value.copy(comments = comments) }
        }
    }

    fun addComment(commentMessage: String) {
        viewModelScope.launchWithLoading {
            try {
                id++
                val newComment =
                    Comment(id = id.toString(), message = commentMessage)
                Log.v("QWE", "viewModel.addComment: $id $commentMessage")
                repository.save(newComment)
            } catch (e: Exception) {
                showError(e)
            }
        }
    }

    fun removeComment(commentId: String) {
        viewModelScope.launchWithLoading {
            try {
                val commentToRemove = state.value.comments.find { it.id == commentId } ?: return@launchWithLoading
                Log.v("QWE", "viewModel.removeComment ID: $commentId" +"commecommentToRemove: "+ commentToRemove)
                repository.remove(commentToRemove)
            } catch (e: Exception) {
                showError(e)
            }
        }
    }

    fun showComment() {
        viewModelScope.launchWithLoading {
            repository.getComments()
        }
    }

    private suspend fun showError(exception: Exception) {
        state.value = state.value.copy(errorMessage = exception.toString())
        Log.v("QWE", exception.toString())
        delay(1500)
        state.value = state.value.copy(errorMessage = null)
    }

    private fun CoroutineScope.launchWithLoading(
        block: suspend () -> Unit
    ) = launch {
        try {
            state.value = state.value.copy(isLoading = true)
            block()
        } finally {
            state.value = state.value.copy(isLoading = false)
        }
    }
}