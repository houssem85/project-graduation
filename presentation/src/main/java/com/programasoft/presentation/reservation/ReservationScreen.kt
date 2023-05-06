@file:OptIn(ExperimentalMaterial3Api::class)

package com.programasoft.presentation.reservation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ReservationRoute(
    viewModel: ReservationViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ReservationScreen(
        reservationUiState = uiState,
        onSelectDate = viewModel::selectDate
    )
}


@Composable
fun ReservationScreen(
    reservationUiState: ReservationUiState,
    onSelectDate: (Long) -> Unit = {},
) {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        if (reservationUiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(
                    Alignment.Center
                )
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                val datePickerState = rememberDatePickerState()
                LaunchedEffect(datePickerState.selectedDateMillis) {
                    Toast.makeText(context, "jrtg", Toast.LENGTH_LONG).show()
                    datePickerState.selectedDateMillis?.let {
                        onSelectDate(
                            it
                        )
                    }
                }
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier.padding(16.dp),
                    dateValidator = { date ->
                        reservationUiState.availableDates.contains(date)
                    }
                )
                Text("Selected date timestamp: ${datePickerState.selectedDateMillis ?: "no selection"}")
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2)
                ) {
                    items(reservationUiState.availabilityUnits.size) { photo ->
                        AssistChip(
                            onClick = { /* Do something! */ },
                            label = { Text("Assist Chip") },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Settings,
                                    contentDescription = "Localized description",
                                    Modifier.size(AssistChipDefaults.IconSize)
                                )
                            }
                        )
                    }
                }

            }
        }
    }
}