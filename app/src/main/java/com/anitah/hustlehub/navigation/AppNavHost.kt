package com.anitah.hustlehub.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anitah.hustlehub.ui.screens.auth.LoginScreen
import com.anitah.hustlehub.ui.screens.auth.RegisterScreen
import com.anitah.hustlehub.ui.screens.home.HomeScreen
import com.anitah.hustlehub.ui.screens.postskill.PostSkillScreen
import com.anitah.hustlehub.ui.screens.profile.ProfileScreen
import com.anitah.hustlehub.ui.screens.skilldetail.SkillDetailScreen
import com.anitah.hustlehub.ui.screens.splash.SplashScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_SPLASH
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(ROUT_SPLASH) {
            SplashScreen(navController)
        }

        composable(ROUT_REGISTER) {
            RegisterScreen(navController)
        }

        composable(ROUT_LOGIN) {
            LoginScreen(navController)
        }

        composable(ROUT_HOME) {
            HomeScreen(navController)
        }

        composable(ROUT_POSTSKILL) {
            PostSkillScreen(navController)
        }

        composable("skilldetail/{title}/{category}/{description}/{price}/{location}/{postedBy}") { backStackEntry ->
            SkillDetailScreen(
                navController = navController,
                title = backStackEntry.arguments?.getString("title") ?: "",
                category = backStackEntry.arguments?.getString("category") ?: "",
                description = backStackEntry.arguments?.getString("description") ?: "",
                price = backStackEntry.arguments?.getString("price") ?: "",
                location = backStackEntry.arguments?.getString("location") ?: "",
                postedBy = backStackEntry.arguments?.getString("postedBy") ?: ""
            )
        }

        composable(ROUT_PROFILE) {
            ProfileScreen(navController)
        }
    }
}