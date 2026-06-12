package com.cfa.cda.catapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cfa.cda.catapp.ui.breeds.BreedsListScreen
import com.cfa.cda.catapp.ui.detail.BreedDetailScreen
import com.cfa.cda.catapp.ui.favorites.FavoritesScreen
import com.cfa.cda.catapp.ui.home.HomeScreen
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import com.cfa.cda.catapp.ui.mycats.MyCatsScreen
import androidx.compose.material3.Text
import com.cfa.cda.catapp.ui.mycats.MyCatFormScreen

object Routes {
    const val HOME = "home"
    const val BREEDS = "breeds"
    const val FAVORITES = "favorites"
    const val MY_CATS = "my_cats"
    const val DETAIL = "detail/{breedId}"
    const val MY_CAT_FORM = "my_cat_form/{catId}"
    fun detail(breedId: String) = "detail/$breedId"
    fun myCatForm(catId: Long) = "my_cat_form/$catId"
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { it }) + fadeIn()
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -it / 3 }) + fadeOut()
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -it / 3 }) + fadeIn()
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
        }
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onBreedClick = { breedId -> navController.navigate(Routes.detail(breedId)) }
            )
        }
        composable(Routes.BREEDS) {
            BreedsListScreen(
                onBreedClick = { breedId -> navController.navigate(Routes.detail(breedId)) }
            )
        }
        composable(Routes.FAVORITES) {
            FavoritesScreen(
                onBreedClick = { breedId -> navController.navigate(Routes.detail(breedId)) }
            )
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("breedId") { type = NavType.StringType })
        ) { backStackEntry ->
            val breedId = backStackEntry.arguments?.getString("breedId") ?: ""
            BreedDetailScreen(
                breedId = breedId,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.MY_CATS) {
            MyCatsScreen(
                onAddClick = { navController.navigate(Routes.myCatForm(0L)) },
                onCatClick = { catId -> navController.navigate(Routes.myCatForm(catId)) }
            )
        }
        composable(
            route = Routes.MY_CAT_FORM,
            arguments = listOf(navArgument("catId") { type = NavType.LongType })
        ) { backStackEntry ->
            val catId = backStackEntry.arguments?.getLong("catId") ?: 0L
            MyCatFormScreen(
                catId = catId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}