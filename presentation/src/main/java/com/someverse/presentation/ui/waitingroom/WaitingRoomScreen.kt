package com.someverse.presentation.ui.waitingroom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.someverse.domain.model.Chat
import com.someverse.presentation.components.SimpleTopBar
import com.someverse.presentation.ui.chat.ChatListItem
import com.someverse.presentation.ui.theme.Background
import com.someverse.presentation.ui.theme.Divider
import com.someverse.presentation.ui.theme.PrimaryPurple

/**
 * Waiting Room Screen
 * - Screen showing pending chats waiting for approval
 */
@Composable
fun WaitingRoomScreen(
    onBackClick: () -> Unit,
    viewModel: WaitingRoomViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = "대기방",
                onBackClick = onBackClick,
                backgroundColor = Background
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Background)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = PrimaryPurple
                    )
                }

                uiState.error != null -> {
                    Text(
                        text = uiState.error ?: "Error",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Red
                    )
                }

                else -> {
                    WaitingRoomContent(
                        chatList = uiState.chatList
                    )
                }
            }
        }
    }
}

@Composable
private fun WaitingRoomContent(
    chatList: List<Chat>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(chatList) { chat ->
            ChatListItem(chat = chat)
            HorizontalDivider(
                color = Divider,
                thickness = 1.dp
            )
        }
    }
}