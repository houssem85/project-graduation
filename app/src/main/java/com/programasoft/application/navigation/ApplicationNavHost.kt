package com.programasoft.application.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.programasoft.application.main.MainRoute
import com.programasoft.presentation.login.LoginClientRoute
import com.programasoft.presentation.register.RegisterClientRoute
import com.programasoft.presentation.video.VideoRoute

@Composable
fun ApplicationNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = "login_client_route",
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val sharedPref = context.getSharedPreferences("project-graduation", Context.MODE_PRIVATE)
        val clientId = sharedPref.getLong("client_id", 0)
        if (clientId != 0L) {
            navController.popBackStack()
            navController.navigate("main_route")
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(
            "login_client_route",
        ) {
            LoginClientRoute(
                onSignUpClicked = {
                    navController.navigate("register_client_route")
                },
                onUserLoggedIn = {
                    navController.popBackStack()
                    navController.navigate("main_route")
                }
            )
        }

        composable(
            "register_client_route",
        ) {
            RegisterClientRoute(onUserLoggedIn = {
                navController.popBackStack()
                navController.navigate("main_route")
            })
        }

        composable(
            "main_route",
        ) {
            MainRoute(
                onJoinConsultation = {
                    navController.navigate("video_route/$it")
                },
                onLogOutClicked = {
                    navController.popBackStack()
                    navController.navigate("login_client_route")
                },
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            "video_route/{reservationId}",
            arguments = listOf(navArgument("reservationId") { type = NavType.LongType })
        ) {
            VideoRoute()
        }
    }
}
