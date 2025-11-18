package com.someverse.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.someverse.presentation.R
import com.someverse.presentation.ui.theme.*

/**
 * 심플한 TopBar 컴포넌트
 * @param title 중앙에 표시할 제목
 * @param onBackClick 뒤로가기 버튼 클릭 시 콜백 (null이면 뒤로가기 버튼 숨김)
 * @param backgroundColor 배경색
 */
@Composable
fun SimpleTopBar(
    modifier: Modifier = Modifier,
    title: String = "",
    onBackClick: (() -> Unit)? = null,
    backgroundColor: Color = Color.Transparent
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(backgroundColor)
            .padding(horizontal = Dimensions.space16),
        contentAlignment = Alignment.Center
    ) {
        // 뒤로가기 버튼 (왼쪽)
        if (onBackClick != null) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dismiss),
                    contentDescription = "뒤로가기",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF9098A6)
                )
            }
        }

        // 제목 (중앙)
        if (title.isNotEmpty()) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    lineHeight = 28.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = PretendardFontFamily
                ).withLetterSpacingPercent(-2.5f),
                color = Black,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

/**
 * 액션 버튼이 있는 TopBar 컴포넌트
 * @param title 중앙에 표시할 제목
 * @param onBackClick 뒤로가기 버튼 클릭 시 콜백 (null이면 뒤로가기 버튼 숨김)
 * @param actionText 오른쪽 액션 버튼 텍스트 (null이면 액션 버튼 숨김)
 * @param actionIconRes 오른쪽 액션 아이콘 리소스 (null이면 아이콘 숨김, actionText보다 우선)
 * @param onActionClick 액션 버튼 클릭 시 콜백
 * @param actionEnabled 액션 버튼 활성화 여부
 * @param backgroundColor 배경색
 */
@Composable
fun TopBarWithAction(
    modifier: Modifier = Modifier,
    title: String = "",
    onBackClick: (() -> Unit)? = null,
    actionText: String? = null,
    actionIconRes: Int? = null,
    onActionClick: (() -> Unit)? = null,
    actionEnabled: Boolean = true,
    backgroundColor: Color = Color.Transparent
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(backgroundColor)
            .padding(horizontal = Dimensions.space16),
        contentAlignment = Alignment.Center
    ) {
        // 뒤로가기 버튼 (왼쪽)
        if (onBackClick != null) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dismiss),
                    contentDescription = "뒤로가기",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF9098A6)
                )
            }
        }

        // 제목 (중앙)
        if (title.isNotEmpty()) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    lineHeight = 28.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = PretendardFontFamily
                ).withLetterSpacingPercent(-2.5f),
                color = Black,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // 액션 버튼 (오른쪽) - 아이콘 또는 텍스트
        if (onActionClick != null) {
            when {
                actionIconRes != null -> {
                    // 아이콘 액션
                    IconButton(
                        onClick = onActionClick,
                        enabled = actionEnabled,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            painter = painterResource(id = actionIconRes),
                            contentDescription = "액션",
                            modifier = Modifier.size(24.dp),
                            tint = if (actionEnabled) Color(0xFF616772) else DescGray
                        )
                    }
                }

                actionText != null -> {
                    // 텍스트 액션
                    Text(
                        text = actionText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontFamily = PretendardFontFamily
                        ).withLetterSpacingPercent(-2.5f),
                        color = if (actionEnabled) PrimaryPurple else DescGray,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable(enabled = actionEnabled) { onActionClick() }
                    )
                }
            }
        }
    }
}