@file:OptIn(ExperimentalMaterial3Api::class)

package com.programasoft.application.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun MainRoute(
    onJoinConsultation: (Long) -> Unit,
    onBackClicked: () -> Unit,
    onLogOutClicked: () -> Unit
) {
    val navController = rememberNavController()
    MainScreen(
        navController,
        onJoinConsultation,
        onBackClicked = onBackClicked,
        onLogOutClicked = onLogOutClicked
    )
}


@Composable
fun MainScreen(
    navController: NavHostController,
    onJoinConsultation: (Long) -> Unit,
    onLogOutClicked: () -> Unit,
    onBackClicked: () -> Unit,
) {
    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) }
    ) {

        MainNavHost(
            navController = navController,
            modifier = Modifier.padding(paddingValues = it),
            onJoinConsultation = onJoinConsultation,
            onLogOutClicked = onLogOutClicked,
            onBackClicked = onBackClicked
        )
    }
}