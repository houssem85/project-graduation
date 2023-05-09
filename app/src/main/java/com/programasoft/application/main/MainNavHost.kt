package com.programasoft.application.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.programasoft.presentation.psychologistprofile.PsychologistProfileRoute
import com.programasoft.presentation.psychologists.PsychologistsRoute
import com.programasoft.presentation.reservation.ReservationRoute
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
            PsychologistsRoute(
                onClick = {
                    navController.navigate("psychologist_profile_route/${it}")
                }
            )
        }
        composable("reservations_route") {
            ReservationsRoute()
        }
        composable(
            "psychologist_profile_route/{psychologistId}",
            arguments = listOf(navArgument("psychologistId") { type = NavType.IntType })
        ) {
            PsychologistProfileRoute(
                onClickReservation = {
                    navController.navigate("reservation_route/$it")
                }
            )
        }
        composable(
            "reservation_route/{psychologistId}",
            arguments = listOf(navArgument("psychologistId") { type = NavType.IntType })
        ) {
            ReservationRoute(onReservationSuccess = {
                navController.popBackStack()
            })
        }
    }
}