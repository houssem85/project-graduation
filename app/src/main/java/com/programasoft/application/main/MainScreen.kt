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
fun MainRoute() {
    val navController = rememberNavController()
    MainScreen(
        navController
    )
}


@Composable
fun MainScreen(
    navController: NavHostController,
) {
    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) }
    ) {

        MainNavHost(
            navController = navController,
            modifier = Modifier.padding(paddingValues = it)
        )
    }
}