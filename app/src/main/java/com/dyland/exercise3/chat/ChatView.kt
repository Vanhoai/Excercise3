package com.dyland.exercise3.chat

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dyland.exercise3.Message
import com.google.ai.client.generativeai.type.Content

@Composable
fun ChatView(modifier: Modifier = Modifier,viewModel: ChatViewModel, context: Context) {

    val chatUIState = viewModel.chatUIState.collectAsState().value
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val listState = rememberLazyListState()
    val valueTextField = remember {
        mutableStateOf("")
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
        ) {
            item {
                Spacer(modifier = Modifier.height(40.dp))
                Text(text = "Chatbot", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(20.dp))
            }

            makeMessages(chatUIState.messages, screenWidth)
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column {
                AnimatedVisibility(visible = chatUIState.isLoading) {
                    Text(text = "Typing ... ", modifier=Modifier.padding(12.dp))
                }
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    value = valueTextField.value,
                    onValueChange = {
                        valueTextField.value = it
                    },
                    singleLine = false,
                    label = {
                        Text(text = "Type something")
                    },
                    trailingIcon = {
                        Text(
                            text = "Send",
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.DarkGray)
                                .padding(8.dp)
                                .clickable {
                                    if (valueTextField.value.isEmpty()) {
                                        val toast = Toast.makeText(
                                            context,
                                            "Please type something",
                                            Toast.LENGTH_SHORT
                                        )
                                        toast.show()
                                        return@clickable
                                    }

                                    viewModel.onEvent(ChatUIEvents.SendMessage(valueTextField.value))
                                    valueTextField.value = ""
                                }
                        )
                    }
                )
            }
        }
    }
}

fun LazyListScope.makeMessages(messages: List<Message>, screenWidth: Dp) {
    items(messages) {
        val margin = if (it.role == "user") {
            120.dp
        } else {
            60.dp
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = if (it.role == "user") {
                Arrangement.End
            } else {
                Arrangement.Start
            },
        ) {
            Column (
                modifier = Modifier
                    .width(screenWidth - margin)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray.copy(alpha = 0.2f))
                    .padding(12.dp)
            ) {
                Text(
                    text = it.message,
                )
            }
        }
    }
}