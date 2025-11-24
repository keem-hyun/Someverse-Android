package com.someverse.presentation.navigation

import androidx.annotation.DrawableRes
import com.someverse.presentation.R

/**
 * Bottom Navigation Items
 * - Defines tab bar items and their properties
 */
sealed class BottomNavItem(
    val route: String,
    val title: String,
    @param:DrawableRes val icon: Int
) {
    data object MyProfile : BottomNavItem(
        route = Screen.MyProfile.route,
        title = "my",
        icon = R.drawable.ic_my_profile
    )

    data object Feed : BottomNavItem(
        route = Screen.Feed.route,
        title = "탐색",
        icon = R.drawable.ic_search
    )

    data object Matching : BottomNavItem(
        route = Screen.Matching.route,
        title = "매칭",
        icon = R.drawable.ic_match
    )

    data object Chat : BottomNavItem(
        route = Screen.Chat.route,
        title = "채팅",
        icon = R.drawable.ic_chat
    )

    companion object {
        val items = listOf(
            MyProfile,
            Feed,
            Matching,
            Chat
        )
    }
}