package com.programasoft.application.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Paid
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
        val icon: ImageVector,
        val titleText: String,
        val screenRoute: String,
) {
    PSYCHOLOGISTS(
            icon = Icons.Filled.Group,
            titleText = "Psychologists",
            screenRoute = "psychologists_route"
    ),
    RESERVATIONS(
            icon = Icons.Filled.BookOnline,
            titleText = "Reservations",
            screenRoute = "reservations_route"
    ),
    ACCOUNT_BALANCE(
            icon = Icons.Filled.Paid,
            titleText = "Account Balance",
            screenRoute = "balance_route"
    )
}