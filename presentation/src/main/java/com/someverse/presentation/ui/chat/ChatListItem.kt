package com.someverse.presentation.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.someverse.domain.model.Chat
import com.someverse.presentation.components.UnreadBadge
import com.someverse.presentation.ui.theme.Background
import com.someverse.presentation.ui.theme.Black
import com.someverse.presentation.ui.theme.ChipGray

@Composable
fun ChatListItem(
    chat: Chat,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Background)
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 12.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile image and text content in a row
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile image
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEBEFF5))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Text content
            Column {
                Text(
                    text = chat.partnerNickname,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Black,
                    letterSpacing = (-0.3).sp,
                    lineHeight = (16 * 1.2).sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = chat.lastMessage ?: "",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Black,
                    letterSpacing = (-0.3).sp,
                    lineHeight = (14 * 1.43).sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.width(24.dp))

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

