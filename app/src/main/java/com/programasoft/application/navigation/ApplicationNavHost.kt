package com.programasoft.application.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.programasoft.presentation.login.LoginClientRoute
import com.programasoft.presentation.register.RegisterClientRoute

@Composable
fun ApplicationNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = "login_client_route",
) {

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
                }
            )
        }

        composable(
            "register_client_route",
        ) {
            RegisterClientRoute()
        }
    }
}
