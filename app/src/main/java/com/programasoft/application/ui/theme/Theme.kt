package com.programasoft.application.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable


@Composable
fun ApplicationTheme(content: @Composable () -> Unit) {

    MaterialTheme(
        content = content
    )
}