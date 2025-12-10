package com.someverse.presentation.ui.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.someverse.presentation.navigation.BottomNavItem
import com.someverse.presentation.navigation.Screen
import com.someverse.presentation.ui.chat.ChatScreen
import com.someverse.presentation.ui.chat.DetailChatScreen
import com.someverse.presentation.ui.feed.CreateFeedScreen
import com.someverse.presentation.ui.feed.FeedScreen
import com.someverse.presentation.ui.matching.MatchingScreen
import com.someverse.presentation.ui.myprofile.MyProfileScreen
import com.someverse.presentation.ui.theme.PrimaryPurple
import com.someverse.presentation.ui.waitingroom.WaitingRoomScreen

/**
 * Main Screen with Bottom Navigation
 * - Contains tab bar and nested navigation
 */
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Bottom navigation을 숨길 화면들
    val screensWithoutBottomBar = listOf(
        Screen.WaitingRoom.route,
        Screen.DetailChat.route,
        Screen.CreateFeed.route
    )
    val shouldShowBottomBar = currentRoute !in screensWithoutBottomBar

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Feed.route,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            composable(Screen.MyProfile.route) {
                MyProfileScreen()
            }

            composable(Screen.Feed.route) {
                FeedScreen(
                    onAddFeedClick = {
                        navController.navigate(Screen.CreateFeed.route)
                    }
                )
            }

            composable(Screen.CreateFeed.route) {
                CreateFeedScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onFeedCreated = {
                        navController.popBackStack()
                    },
                    onSearchClick = {
                        // TODO: Navigate to movie search screen
                    }
                )
            }

            composable(Screen.Matching.route) {
                MatchingScreen()
            }

            composable(Screen.Chat.route) {
                ChatScreen(
                    onNavigateToWaitingRoom = {
                        navController.navigate(Screen.WaitingRoom.route)
                    },
                    onNavigateToDetailChat = { roomId ->
                        navController.navigate(
                            Screen.DetailChat.createRoute(
                                roomId
                            )
                        )
                    }
                )
            }

            composable(Screen.WaitingRoom.route) {
                WaitingRoomScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Screen.DetailChat.route,
                arguments = listOf(
                    navArgument("roomId") { type = NavType.LongType }
                )
            ) {
                DetailChatScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = Color.White,
        contentColor = PrimaryPurple
    ) {
        BottomNavItem.items.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.route == item.route
            } == true

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = stringResource(id = item.titleRes)
                    )
                },
                label = {
                    Text(text = stringResource(id = item.titleRes))
                },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        // 같은 아이템 재클릭시 스택 정리
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // 같은 destination 중복 방지
                        launchSingleTop = true
                        // 상태 복원
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryPurple,
                    selectedTextColor = PrimaryPurple,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}