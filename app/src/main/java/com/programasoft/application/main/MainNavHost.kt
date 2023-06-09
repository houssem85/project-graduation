package com.programasoft.application.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.programasoft.presentation.accountbalancetransaction.AccountBalanceTransactionRoute
import com.programasoft.presentation.join.JoinRoute
import com.programasoft.presentation.paymee.PaymeeRoute
import com.programasoft.presentation.psychologistprofile.PsychologistProfileRoute
import com.programasoft.presentation.psychologists.PsychologistsRoute
import com.programasoft.presentation.reservation.ReservationRoute
import com.programasoft.presentation.reservations.ReservationsRoute
import com.programasoft.presentation.video.VideoRoute

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = "psychologists_route",
    onJoinConsultation: (Long) -> Unit,
    onLogOutClicked: () -> Unit,
    onBackClicked: () -> Unit,
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
                },
                onBackClicked = onBackClicked,
                onLogOutClicked = onLogOutClicked
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
                }, onBackClicked = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            "reservation_route/{psychologistId}",
            arguments = listOf(navArgument("psychologistId") { type = NavType.IntType })
        ) {
            ReservationRoute(onReservationSuccess = {
                navController.popBackStack()
            }, onBackClicked = {
                navController.popBackStack()
            })
        }
        composable(
            "balance_route"
        ) {
            AccountBalanceTransactionRoute(
                onEnterAmount = {
                    navController.navigate("paymee_route/$it")
                }
            )
        }
        composable(
            "paymee_route/{amount}",
            arguments = listOf(navArgument("amount") { type = NavType.FloatType })
        ) {
            PaymeeRoute(onFinish = {
                navController.popBackStack()
            })
        }
        composable(
            "join_route",
        ) {
            JoinRoute(onJoinConsultation = onJoinConsultation)
        }
    }
}