package com.programasoft.application.main


import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigation(navController: NavHostController) {
    NavigationBar(
        containerColor = Color(0xFF3F5AA6),
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        TopLevelDestination.values().forEach {
            NavigationBarItem(
                icon = { Icon(it.icon, contentDescription = null) },
                label = {
                    Text(
                        text = it.titleText,
                        fontSize = 9.sp
                    )
                },
                alwaysShowLabel = true,
                selected = currentRoute == it.screenRoute,
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = Color.White.copy(alpha = 0.5f),
                    selectedIconColor = Color.White,
                    indicatorColor = Color(0xFF788BC0),
                    disabledTextColor = Color.White.copy(alpha = 0.5f),
                    unselectedTextColor = Color.White.copy(alpha = 0.5f),
                    selectedTextColor = Color.White
                ),
                onClick = {
                    navController.navigate(it.screenRoute) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}