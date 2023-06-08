package com.programasoft.application.psychologist.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.programasoft.presentation.accountbalancetransaction.AccountBalanceTransactionRoute
import com.programasoft.presentation.availabilities.AvailabilitiesRoute
import com.programasoft.presentation.newavailabilitygroup.NewAvailabilityGroupRoute
import com.programasoft.presentation.paymee.PaymeeRoute
import com.programasoft.presentation.psychologistprofile.PsychologistProfileRoute
import com.programasoft.presentation.psychologists.PsychologistsRoute
import com.programasoft.presentation.reservation.ReservationRoute
import com.programasoft.presentation.reservations.ReservationsRoute

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = "balance_route",
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
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
            "availabilities_route"
        ) {
            AvailabilitiesRoute(
                onAddClicked = {
                    navController.navigate("new_availability_group_route")
                }
            )
        }

        composable(
            "new_availability_group_route"
        ) {
            NewAvailabilityGroupRoute()
        }
    }
}