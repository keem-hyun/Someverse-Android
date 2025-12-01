package com.someverse.presentation.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.someverse.domain.model.ChatMessage
import com.someverse.presentation.R
import com.someverse.presentation.components.TopBarWithAction
import com.someverse.presentation.ui.theme.ChatBackground
import com.someverse.presentation.ui.theme.ChatInputBackground
import com.someverse.presentation.ui.theme.ChipGray
import com.someverse.presentation.ui.theme.PretendardFontFamily
import com.someverse.presentation.ui.theme.PrimaryPurple
import com.someverse.presentation.ui.theme.White
import com.someverse.presentation.ui.theme.withLetterSpacingPercent
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Detail Chat Screen
 * - Displays chat messages with partner
 * - Shows date separators
 * - Message input field at bottom
 */
@Composable
fun DetailChatScreen(
    onBackClick: () -> Unit,
    viewModel: DetailChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Scroll to bottom when new message is added
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopBarWithAction(
                title = stringResource(R.string.chat_title),
                onBackClick = onBackClick,
                actionIconRes = R.drawable.ic_report,
                onActionClick = { /* TODO: Open notifications */ },
                backgroundColor = Color.White
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(ChatBackground)
        ) {
            when {
                uiState.isLoading && uiState.messages.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = PrimaryPurple
                    )
                }

                uiState.error != null && uiState.messages.isEmpty() -> {
                    Text(
                        text = uiState.error ?: "Error",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Red
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        MessageList(
                            messages = uiState.messages,
                            partnerProfileImage = uiState.partnerProfileImage,
                            listState = listState,
                            onLoadMore = { viewModel.loadMoreMessages() },
                            modifier = Modifier.weight(1f)
                        )

                        MessageInputBar(
                            messageText = messageText,
                            onMessageChange = { messageText = it },
                            onSendClick = {
                                viewModel.sendMessage(messageText)
                                messageText = ""
                            },
                            isSending = uiState.isSending
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageList(
    messages: List<ChatMessage>,
    partnerProfileImage: String?,
    listState: androidx.compose.foundation.lazy.LazyListState,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Group messages by date
        val groupedMessages = messages.groupBy { message ->
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val date = inputFormat.parse(message.createdAt)
                date?.let {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    dateFormat.format(it)
                }
            } catch (e: Exception) {
                null
            }
        }

        groupedMessages.forEach { (dateString, messagesForDate) ->
            // Date separator
            if (dateString != null) {
                item {
                    val displayFormat = try {
                        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val date = inputFormat.parse(dateString)
                        val outputFormat = SimpleDateFormat("yyyy.MM.dd(E)", Locale.KOREAN)
                        date?.let { outputFormat.format(it) } ?: dateString
                    } catch (e: Exception) {
                        dateString
                    }
                    DateSeparator(date = displayFormat)
                }
            }

            // Messages for this date
            items(messagesForDate) { message ->
                MessageItem(
                    message = message,
                    partnerProfileImage = partnerProfileImage
                )
            }
        }
    }

    // Load more when scrolled to top
    val shouldLoadMore by remember {
        derivedStateOf {
            val firstVisibleItemIndex = listState.firstVisibleItemIndex
            firstVisibleItemIndex < 5
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }
}

@Composable
private fun DateSeparator(date: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        // Left line
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .align(Alignment.CenterVertically)
                .background(Color(0xFFF3F5F7))
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Date text
        Text(
            text = date,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 14.4.sp,
                color = ChipGray
            ).withLetterSpacingPercent(-2.5f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Right line
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .align(Alignment.CenterVertically)
                .background(Color(0xFFF3F5F7))
        )
    }
}

@Composable
private fun MessageItem(
    message: ChatMessage,
    partnerProfileImage: String?
) {
    // Determine if this is my message (assuming senderId != current user for demo)
    // TODO: Get current user ID from auth state
    val isMyMessage = message.senderId == 999 // Temporary logic

    if (isMyMessage) {
        // My message (right aligned, purple background)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFFEBE2FF),
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 0.dp,
                            bottomStart = 8.dp,
                            bottomEnd = 8.dp
                        )
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        lineHeight = 14.4.sp,
                        color = Color(0xFF050505)
                    )
                )
            }
        }
    } else {
        // Partner's message (left aligned, pink background)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            // Profile image
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD9D9D9))
            ) {
                // TODO: Load actual profile image
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Message bubble
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFFFFE6F3),
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 8.dp,
                            bottomStart = 8.dp,
                            bottomEnd = 8.dp
                        )
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        lineHeight = 14.4.sp,
                        color = Color.Black
                    )
                )
            }
        }
    }
}

@Composable
private fun MessageInputBar(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit,
    isSending: Boolean
) {
    val density = LocalDensity.current
    val imeInsets = WindowInsets.ime
    val navBarInsets = WindowInsets.navigationBars

    // 키보드 높이에서 네비게이션 바 높이를 빼서 순수 키보드 높이만 계산
    val imeHeight = with(density) { imeInsets.getBottom(density).toDp() }
    val navBarHeight = with(density) { navBarInsets.getBottom(density).toDp() }
    val keyboardPadding = (imeHeight - navBarHeight).coerceAtLeast(0.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ChatInputBackground)
            .padding(bottom = keyboardPadding)
            .padding(horizontal = 13.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Input field
        BasicTextField(
            value = messageText,
            onValueChange = onMessageChange,
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .background(White, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = PretendardFontFamily,
                fontSize = 14.sp,
                color = Color.Black
            ),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box {
                    if (messageText.isEmpty()) {
                        Text(
                            text = "메시지를 입력하세요",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = PretendardFontFamily,
                                color = ChipGray,
                                fontSize = 14.sp
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )

        // Send button
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = if (messageText.isNotBlank()) PrimaryPurple else Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(8.dp)
                )
                .then(
                    if (messageText.isNotBlank() && !isSending) {
                        Modifier.clickable(onClick = onSendClick)
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_send_message),
                contentDescription = "전송",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}