package com.programasoft.application.psychologist.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Paid
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val icon: ImageVector,
    val titleText: String,
    val screenRoute: String,
) {
    ACCOUNT_BALANCE(
        icon = Icons.Filled.Paid,
        titleText = "Account Balance",
        screenRoute = "balance_route"
    ),
    AVAILABILITIES(
        icon = Icons.Filled.EventNote,
        titleText = "Availabilities",
        screenRoute = "availabilities_route"
    )
}