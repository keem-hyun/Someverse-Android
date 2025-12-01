package com.someverse.presentation.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.someverse.domain.model.Chat
import com.someverse.domain.model.ChatStatus
import com.someverse.presentation.components.UnreadBadge
import com.someverse.presentation.ui.theme.Background
import com.someverse.presentation.ui.theme.ChipGray
import com.someverse.presentation.ui.theme.Divider
import com.someverse.presentation.ui.theme.GradationEnd
import com.someverse.presentation.ui.theme.GradationStart
import com.someverse.presentation.ui.theme.PrimaryPurple
import com.someverse.presentation.ui.theme.TextPrimary

/**
 * Chat Top Bar
 * - Simple top bar with Someverse title
 */
@Composable
private fun ChatTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Background)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Someverse",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF616772),
            letterSpacing = (-0.5).sp,
            lineHeight = 32.sp
        )
    }
}

/**
 * Chat Screen
 * - Chat list view with pending and active chats
 */
@Composable
fun ChatScreen(
    onNavigateToWaitingRoom: () -> Unit,
    onNavigateToDetailChat: (Long) -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ChatTopBar()
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
                    ChatListContent(
                        chatList = uiState.chatList,
                        onPendingChatClick = onNavigateToWaitingRoom,
                        onChatClick = { roomId ->
                            onNavigateToDetailChat(roomId.toLong())
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatListContent(
    chatList: List<Chat>,
    onPendingChatClick: () -> Unit,
    onChatClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Pending chat (special card)
        val pendingChat = chatList.firstOrNull { it.status == ChatStatus.PENDING }
        if (pendingChat != null) {
            item {
                PendingChatCard(
                    chat = pendingChat,
                    onClick = onPendingChatClick
                )
            }
        }

        // Active chats
        val activeChats = chatList.filter { it.status == ChatStatus.ACTIVE }
        items(activeChats) { chat ->
            ChatListItem(
                chat = chat,
                onClick = { onChatClick(chat.roomId) }
            )
            HorizontalDivider(
                color = Divider,
                thickness = 1.dp
            )
        }
    }
}

@Composable
private fun PendingChatCard(
    chat: Chat,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GradationStart,
                        GradationEnd
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = Background,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEBEFF5))
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Text content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = chat.partnerNickname,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    letterSpacing = (-0.3).sp,
                    lineHeight = (16 * 1.2).sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = chat.lastMessage ?: "",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                    letterSpacing = (-0.3).sp,
                    lineHeight = (14 * 1.43).sp
                )
            }

            // Right side (time and badge)
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (chat.unreadCount > 0) {
                    UnreadBadge(count = chat.unreadCount)
                }
                Text(
                    text = "2분 전",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = ChipGray,
                    letterSpacing = (-0.3).sp,
                    lineHeight = (12 * 1.2).sp
                )
            }
        }
    }
}