@file:OptIn(ExperimentalMaterial3Api::class)

package com.programasoft.presentation.newavailabilitygroup

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun NewAvailabilityGroupRoute(
    viewModel: NewAvailabilityGroupViewModel = hiltViewModel()
) {
    val uiState: NewAvailabilityGroupUiState by viewModel.uiState.collectAsStateWithLifecycle()
    NewAvailabilityGroupScreen(
        uiState,
        onChangeEndDate = viewModel::onSelectEndDate,
        onChangeStartDate = viewModel::onSelectStartDate
    )
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewAvailabilityGroupScreen(
    uiState: NewAvailabilityGroupUiState,
    onChangeStartDate: (String) -> Unit,
    onChangeEndDate: (String) -> Unit
) {

    val openDialogStartDate = remember { mutableStateOf(false) }
    val openDialogEndDate = remember { mutableStateOf(false) }

    val startDatePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val currentDate = System.currentTimeMillis()
                return utcTimeMillis >= currentDate
            }
        }
    )
    val endDatePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val currentDate = System.currentTimeMillis()
                return utcTimeMillis >= currentDate
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Start Date")
                Spacer(
                    modifier = Modifier.size(10.dp)
                )
                Box(
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = Color.Blue,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            openDialogStartDate.value = true
                        }
                ) {
                    Text(text = uiState.startDate, modifier = Modifier.align(Alignment.Center))
                }
            }
            Spacer(modifier = Modifier.size(20.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "End Date")
                Spacer(
                    modifier = Modifier.size(10.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .border(
                            width = 2.dp,
                            color = Color.Blue,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            openDialogEndDate.value = true
                        }
                ) {
                    Text(text = uiState.endDate, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
        if (openDialogStartDate.value) {
            CustomDatePicker(
                onDismissRequest = {
                    openDialogStartDate.value = false
                },
                onSubmit = {
                    openDialogStartDate.value = false
                    onChangeStartDate(it)
                }, datePickerState = startDatePickerState
            )
        }
        if (openDialogEndDate.value) {
            CustomDatePicker(
                onDismissRequest = {
                    openDialogEndDate.value = false
                },
                onSubmit = {
                    openDialogEndDate.value = false
                    onChangeEndDate(it)
                }, datePickerState = endDatePickerState
            )
        }
    }
}

fun convertLongToDate(longDate: Long): String {
    val days = longDate / 86400000 // Nombre de jours écoulés
    val date = LocalDate.ofEpochDay(days)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return date.format(formatter)
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun CustomDatePicker(
    onDismissRequest: () -> Unit,
    onSubmit: (String) -> Unit,
    datePickerState: DatePickerState
) {
    val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    val date = convertLongToDate(datePickerState.selectedDateMillis!!)
                    onSubmit(date)
                },
                enabled = confirmEnabled.value
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
