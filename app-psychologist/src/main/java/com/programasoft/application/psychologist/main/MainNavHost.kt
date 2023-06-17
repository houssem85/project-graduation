package com.programasoft.application.psychologist.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.programasoft.presentation.accountbalancetransaction.AccountBalanceTransactionRoute
import com.programasoft.presentation.availabilities.AvailabilitiesRoute
import com.programasoft.presentation.homepsychologist.HomePsychologistRoute
import com.programasoft.presentation.newavailabilitygroup.NewAvailabilityGroupRoute

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = "home_psychologist_route",
    onJoinConsultation: (Long) -> Unit,
    onLogOutClicked: () -> Unit,
    onBackClicked: () -> Unit,
) {
    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier
    ) {
        composable(
            "balance_route"
        ) {
            AccountBalanceTransactionRoute(onEnterAmount = {
                navController.navigate("paymee_route/$it")
            })
        }
        composable(
            "availabilities_route"
        ) {
            AvailabilitiesRoute(onAddClicked = {
                navController.navigate("new_availability_group_route")
            })
        }

        composable(
            "new_availability_group_route"
        ) {
            NewAvailabilityGroupRoute(onFinish = {
                navController.popBackStack()
            }, onBackClicked = {
                navController.popBackStack()
            })
        }

        composable(
            "home_psychologist_route"
        ) {
            HomePsychologistRoute(
                onBackClicked = onBackClicked,
                onJoinConsultation = onJoinConsultation,
                onLogOutClicked = onLogOutClicked
            )
        }
    }
}