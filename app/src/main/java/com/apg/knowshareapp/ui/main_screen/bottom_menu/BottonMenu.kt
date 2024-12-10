package com.apg.knowshareapp.ui.main_screen.bottom_menu

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun BottomMenu(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    val items = listOf(
        BottomMenuItem.Home,
        BottomMenuItem.Favs,
        BottomMenuItem.Settings
    )

    val selectedItem = remember { mutableStateOf("Home") }

    NavigationBar(
        containerColor = Color(0xFF00BCD5)
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedTab == item.route,
                onClick = {
                    onTabSelected(item.route)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconId),
                        contentDescription = null)
                },
                label = {
                    Text(text = item.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color(0xFF007396),
                    unselectedTextColor = Color(0xFF007396),
                    indicatorColor = Color(0xFF007396)
                )
            )
        }


    }
}