package com.example.test.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.test.repository.datebase.DatabaseProvider
import com.example.test.view.CommentScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseProvider.init(applicationContext)
        setContent {
            CommentScreen()
        }
    }
}


