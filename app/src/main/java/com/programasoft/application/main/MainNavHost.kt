package com.programasoft.application.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.programasoft.presentation.psychologists.PsychologistsRoute
import com.programasoft.presentation.reservations.ReservationsRoute

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = "psychologists_route",
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable("psychologists_route") {
            PsychologistsRoute()
        }
        composable("reservations_route") {
            ReservationsRoute()
        }
    }
}