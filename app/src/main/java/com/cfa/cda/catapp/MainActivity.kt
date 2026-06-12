package com.cfa.cda.catapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cfa.cda.catapp.navigation.NavGraph
import com.cfa.cda.catapp.navigation.Routes
import com.cfa.cda.catapp.ui.components.BottomNavBar
import com.cfa.cda.catapp.ui.components.BottomNavItem
import com.cfa.cda.catapp.ui.theme.CatApiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatApiTheme {
                MainScreen()
            }
        }
    }
}

@androidx.compose.runtime.Composable
fun MainScreen() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(modifier = Modifier.weight(1f)) {
            NavGraph(navController = navController)
        }

        // N'affiche la bottom bar que sur les 3 écrans principaux
        if (currentRoute in listOf(Routes.HOME, Routes.BREEDS, Routes.FAVORITES)) {
            val selected = when (currentRoute) {
                Routes.BREEDS -> BottomNavItem.BREEDS
                Routes.FAVORITES -> BottomNavItem.FAVORITES
                Routes.MY_CATS -> BottomNavItem.MY_CATS
                else -> BottomNavItem.HOME
            }
            BottomNavBar(
                selected = selected,
                onItemSelected = { item ->
                    val route = when (item) {
                        BottomNavItem.HOME -> Routes.HOME
                        BottomNavItem.BREEDS -> Routes.BREEDS
                        BottomNavItem.FAVORITES -> Routes.FAVORITES
                        BottomNavItem.MY_CATS -> Routes.MY_CATS
                    }
                    navController.navigate(route) {
                        popUpTo(Routes.HOME) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}