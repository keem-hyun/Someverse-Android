package com.someverse.presentation.ui.feed

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.someverse.presentation.R
import com.someverse.presentation.ui.theme.*
import kotlin.math.absoluteValue

/**
 * Feed Screen
 * - Main feed view with movie reviews
 */
@Composable
fun FeedScreen(
    viewModel: FeedViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top App Bar
            FeedTopBar()

            // Feed Content
            FeedContent(
                uiState = uiState,
                onRefresh = { viewModel.refresh() },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp)
            )
        }

        // Floating Action Button
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp, top = 84.dp)
                .shadow(
                    elevation = 15.dp,
                    shape = CircleShape,
                    spotColor = Color(0xFFE25061).copy(alpha = 0.21f)
                )
                .size(58.dp)
                .background(Color.White, CircleShape)  // White border background
                .clickable { /* TODO: Handle add action */ },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)  // 조금 작게 (border 효과)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                GradationStart.copy(alpha = 0.3f),
                                GradationEnd.copy(alpha = 0.3f)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_image),
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun FeedTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left icon placeholder
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFFD9D9D9))
        )

        // Someverse title
        Text(
            text = stringResource(id = R.string.someverse),
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                lineHeight = 32.sp
            ).withLetterSpacingPercent(-2.5f),
            color = Color(0xFF616772),
            textAlign = TextAlign.Center
        )

        // Right icon placeholder
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFFD9D9D9))
        )
    }
}

@Composable
fun FeedContent(
    uiState: FeedUiState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        when {
            uiState.isLoading -> {
                // Loading state
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = PrimaryPurple
                )
            }

            uiState.error != null -> {
                // Error state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = uiState.error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onRefresh,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryPurple
                        )
                    ) {
                        Text("다시 시도")
                    }
                }
            }

            else -> {
                // Success state with feeds
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    // Horizontal paging feed with page animation
                    val pagerState = rememberPagerState(
                        initialPage = 0,
                        pageCount = { uiState.feeds.size }
                    )

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 68.dp),
                        pageSpacing = 8.dp
                    ) { page ->
                        val feed = uiState.feeds[page]
                        val pageOffset =
                            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    // 페이지 전환 애니메이션
                                    val scale =
                                        1f - (pageOffset.absoluteValue * 0.15f).coerceAtMost(0.15f)
                                    scaleX = scale
                                    scaleY = scale

                                    // 투명도 효과
                                    alpha =
                                        1f - (pageOffset.absoluteValue * 0.3f).coerceAtMost(0.5f)
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            FeedCard(
                                feed = feed,
                                movieTitle = "어쩔수가없다",
                                movieYear = "(2025)",
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            // Profile image circle
                            Box(
                                modifier = Modifier
                                    .size(108.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFD9D9D9)),
                                contentAlignment = Alignment.Center
                            ) {
                                // TODO: Add profile image here
                            }
                        }
                    }

                    // User profile avatars
                }
            }
        }
    }
}

@Composable
fun FeedCard(
    feed: com.someverse.domain.model.Feed,
    movieTitle: String,
    movieYear: String,
    modifier: Modifier = Modifier
) {
    val feedCardShape = FeedCardShape()
    val cardHeight = 232.dp
    val bottomCurveHeight = 25.dp  // FeedCardShape의 볼록한 높이와 동일
    val shadowOffset = 16.dp  // shadow를 아래로 내리는 거리

    // 외부 Box: Card 높이 + 볼록한 부분 + shadow offset + blur 여유
    Box(
        modifier = modifier
            .height(cardHeight + bottomCurveHeight + shadowOffset + 32.dp)
    ) {
        // Gradient shadow layer (Card + 볼록한 부분까지 포함한 크기)
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight + bottomCurveHeight)  // 볼록한 부분까지 포함
                .offset(y = shadowOffset)
                .blur(radius = 20.dp)
        ) {
            val outline = feedCardShape.createOutline(
                size = Size(
                    width = size.width,  // 전체 너비 사용
                    height = cardHeight.toPx()
                ),
                layoutDirection = layoutDirection,
                density = this
            )
            if (outline is Outline.Generic) {
                drawPath(
                    path = outline.path,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            FeedShaderGradationStart.copy(alpha = 0.6f),
                            FeedShaderGradationEnd.copy(alpha = 0.6f)
                        )
                    )
                )
            }
        }

        // White card (실제 content)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp)
                    .height(cardHeight),
                shape = feedCardShape,  // Shadow와 같은 shape
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp  // gradient shadow 사용
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(18.dp))
                    // Movie info section (at the top of card)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 9.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Movie poster
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFD9D9D9))
                        )

                        Spacer(modifier = Modifier.width(24.dp))

                        // Movie title and year
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = movieTitle,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontFamily = PretendardFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    lineHeight = (16 * 1.2).sp
                                ).withLetterSpacingPercent(-2.5f),
                                color = Color.Black
                            )

                            Text(
                                text = movieYear,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = PretendardFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    lineHeight = (12 * 1.2).sp
                                ).withLetterSpacingPercent(-2.5f),
                                color = Color(0xFF767676)
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Report icon (right side)
                        Icon(
                            painter = painterResource(id = R.drawable.ic_report),
                            contentDescription = "Report",
                            tint = Color(0xFF9098A6),
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { /* TODO: Handle report action */ }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFFBEBF3),
                                        Color(0xFFFCF6E8)
                                    )
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_heart),
                            contentDescription = "Like",
                            tint = Color(0xFFE25061),
                            modifier = Modifier.size(12.dp)
                        )

                        Text(
                            text = "${feed.nickName} 님의 코멘트",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                lineHeight = (12 * 1.2).sp
                            ).withLetterSpacingPercent(-2.5f),
                            color = Color(0xFFE25061)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Review text (from Feed content)
                    Text(
                        text = feed.content,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            lineHeight = (16 * 1.2).sp
                        ).withLetterSpacingPercent(-2.5f),
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}