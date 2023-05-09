@file:OptIn(ExperimentalMaterial3Api::class)

package com.programasoft.presentation.reservation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ReservationRoute(
    viewModel: ReservationViewModel = hiltViewModel(),
    onReservationSuccess: () -> Unit,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isReservationSuccess) {
        if (uiState.isReservationSuccess) {
            Toast.makeText(context, "reservation success", Toast.LENGTH_LONG).show()
            onReservationSuccess()
        }
    }

    ReservationScreen(
        reservationUiState = uiState,
        onSelectDate = viewModel::selectDate,
        onValidateClick = viewModel::validate
    )
}


@Composable
fun ReservationScreen(
    reservationUiState: ReservationUiState,
    onSelectDate: (Long) -> Unit = {},
    onValidateClick: () -> Unit,
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
            LazyColumn(
                modifier = Modifier
                    .matchParentSize()
            ) {
                item {
                    val datePickerState = rememberDatePickerState()
                    LaunchedEffect(datePickerState.selectedDateMillis) {
                        datePickerState.selectedDateMillis?.let {
                            onSelectDate(
                                it
                            )
                        }
                    }
                    DatePicker(
                        state = datePickerState,
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        showModeToggle = false,
                        dateValidator = { date ->
                            reservationUiState.availableDates.contains(date)
                        }
                    )
                }
                item {
                    NonlazyGrid(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(horizontal = 16.dp),
                        columns = 2,
                        itemCount = reservationUiState.availabilityUnits.size
                    ) {
                        val availabilityUnit = reservationUiState.availabilityUnits[it]
                        AssistChip(
                            onClick = {
                                availabilityUnit.onClick.invoke()
                            },
                            label = {
                                Text(
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxSize(),
                                    text = availabilityUnit.getTimeRange()
                                )
                            },
                            enabled = true,
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Timer,
                                    contentDescription = "Localized description",
                                    Modifier.size(AssistChipDefaults.IconSize)
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                labelColor = if (availabilityUnit.isSelected) {
                                    Color.White
                                } else {
                                    Color(0xFF3F5AA6)
                                },
                                containerColor = if (availabilityUnit.isSelected) {
                                    Color(0xFF3F5AA6)
                                } else {
                                    Color.White
                                },
                                leadingIconContentColor = if (availabilityUnit.isSelected) {
                                    Color.White
                                } else {
                                    Color(0xFF3F5AA6)
                                },
                            )

                        )
                    }
                }
                item {
                    val test = reservationUiState.availabilityUnits.any {
                        it.isSelected
                    }
                    if (reservationUiState.errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = reservationUiState.errorMessage,
                            color = Color.Red,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    if (test) {
                        Spacer(modifier = Modifier.size(20.dp))
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF3F5AA6)
                                ),
                                modifier = Modifier.align(Alignment.Center),
                                onClick = onValidateClick
                            ) {
                                Text(text = "Validate")
                            }
                        }
                        Spacer(modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun NonlazyGrid(
    columns: Int,
    itemCount: Int,
    modifier: Modifier = Modifier,
    content: @Composable() (Int) -> Unit,
) {
    Column(modifier = modifier) {
        var rows = (itemCount / columns)
        if (itemCount.mod(columns) > 0) {
            rows += 1
        }

        for (rowId in 0 until rows) {
            val firstIndex = rowId * columns

            Row {
                for (columnId in 0 until columns) {
                    val index = firstIndex + columnId
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp)
                            .weight(1f)
                    ) {
                        if (index < itemCount) {
                            content(index)
                        }
                    }
                }
            }
        }
    }
}