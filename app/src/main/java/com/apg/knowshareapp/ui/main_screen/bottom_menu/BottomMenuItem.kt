package com.apg.knowshareapp.ui.main_screen.bottom_menu

import com.apg.knowshareapp.R

sealed class BottomMenuItem(
    val route: String,
    val title: String,
    val iconId: Int,
) {
    object Home : BottomMenuItem(
        route = "home",
        title = "Home",
        iconId = R.drawable.ic_home
    )
    object Favs : BottomMenuItem(
        route = "favs",
        title = "Favs",
        iconId = R.drawable.ic_favs
    )
    object Settings : BottomMenuItem(
        route = "settings",
        title = "Settings",
        iconId = R.drawable.ic_settings
    )
}