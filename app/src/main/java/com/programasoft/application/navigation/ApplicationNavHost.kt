package com.programasoft.application.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.programasoft.application.main.MainRoute
import com.programasoft.presentation.login.LoginClientRoute
import com.programasoft.presentation.register.RegisterClientRoute

@Composable
fun ApplicationNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = "login_client_route",
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val sharedPref = context.getSharedPreferences("project-graduation", Context.MODE_PRIVATE)
        val clientId = sharedPref.getLong("client_id",0)
        if(clientId != 0L){
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
                    navController.navigate("main_route")
                }
            )
        }

        composable(
            "register_client_route",
        ) {
            RegisterClientRoute()
        }

        composable(
            "main_route",
        ) {
            MainRoute()
        }
    }
}
