package com.example.test.view

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.test.viewModel.CommentsViewModel

@Composable
fun CommentScreen(
    viewModel: CommentsViewModel = viewModel()
) {

//    var comments = viewModel.state.value.comments
//    val isLoading = viewModel.state.value.isLoading
//    val errorMessage = viewModel.state.value.errorMessage
    var newComment by remember { mutableStateOf("") }

    //////
    val state by viewModel.uiState.collectAsState()
    /////
    // }

    Column {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "KOMENTARZE:",
            style = MaterialTheme.typography.titleLarge
        )

        if (state.isLoading) {
            Box(
                modifier = Modifier.background(Color.LightGray.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), contentPadding = PaddingValues(16.dp)
        ) {
            items(state.comments) { comment ->
                Card(
                    modifier = Modifier
                        .defaultMinSize(minHeight = 36.dp)
                        .fillMaxWidth()
                        .border(2.dp, Color.DarkGray, shape = RoundedCornerShape(8.dp))
                    //.padding(6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = comment.message,
                            fontSize = 16.sp,
                        )
                        Icon(imageVector = Icons.Filled.Clear,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable {
                                    Log.v("QWE","remove Click: "+comment.id+"  "+ comment.message)
                                    viewModel.removeComment(comment.id) })

                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        AnimatedVisibility(visible = state.errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Red)
                    .padding(8.dp)
            ) {
                Text(
                    text = state.errorMessage ?: "", color = Color.White
                )
            }
        }
        Button(onClick = { viewModel.showComment() }) {

        }
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(value = newComment,
                onValueChange = { newComment = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                label = { Text("Wprowadź komentarz") })
            Button(
                onClick = {
                    if (newComment.isNotEmpty()) {
                        viewModel.addComment(newComment)
                        newComment = ""
                    }
                }, enabled = newComment.isNotEmpty()
            ) {
                Text("Dodaj")
            }
        }
    }
}