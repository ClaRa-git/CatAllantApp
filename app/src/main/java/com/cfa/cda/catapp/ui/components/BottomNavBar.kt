package com.cfa.cda.catapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.cfa.cda.catapp.ui.theme.CatColorScheme
import com.cfa.cda.catapp.ui.theme.LocalCatColors
import androidx.compose.material.icons.filled.Pets

enum class BottomNavItem(val label: String) {
    HOME("Accueil"),
    BREEDS("Races"),
    FAVORITES("Favoris"),
    MY_CATS("Mes chats")
}

@Composable
fun BottomNavBar(
    selected: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit
) {
    val colors = LocalCatColors.current

    NavigationBar(
        containerColor = colors.card,
        modifier = Modifier.fillMaxWidth()
    ) {
        NavigationBarItem(
            selected = selected == BottomNavItem.HOME,
            onClick = { onItemSelected(BottomNavItem.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Accueil") },
            label = { Text("Accueil", fontSize = 10.sp) },
            colors = navColors(colors)
        )
        NavigationBarItem(
            selected = selected == BottomNavItem.BREEDS,
            onClick = { onItemSelected(BottomNavItem.BREEDS) },
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Races") },
            label = { Text("Races", fontSize = 10.sp) },
            colors = navColors(colors)
        )
        NavigationBarItem(
            selected = selected == BottomNavItem.FAVORITES,
            onClick = { onItemSelected(BottomNavItem.FAVORITES) },
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoris") },
            label = { Text("Favoris", fontSize = 10.sp) },
            colors = navColors(colors)
        )
        NavigationBarItem(
            selected = selected == BottomNavItem.MY_CATS,
            onClick = { onItemSelected(BottomNavItem.MY_CATS) },
            icon = { Icon(Icons.Default.Pets, contentDescription = "Mes chats") },
            label = { Text("Mes chats", fontSize = 10.sp) },
            colors = navColors(colors)
        )
    }
}

@Composable
private fun navColors(colors: CatColorScheme) = NavigationBarItemDefaults.colors(
    selectedIconColor = colors.primary,
    selectedTextColor = colors.primary,
    unselectedIconColor = colors.border,
    unselectedTextColor = colors.border,
    indicatorColor = colors.card
)