package com.dyland.exercise3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.dyland.exercise3.chat.ChatView
import com.dyland.exercise3.chat.ChatViewModel
import com.dyland.exercise3.ui.theme.Exercise3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        setContent {
            Exercise3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChatView(viewModel = chatViewModel, modifier = Modifier.padding(innerPadding), context = applicationContext)
                }
            }
        }
    }
}
