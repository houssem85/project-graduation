@file:OptIn(ExperimentalMaterial3Api::class)

package com.programasoft.presentation.newavailabilitygroup

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun NewAvailabilityGroupRoute(
    viewModel: NewAvailabilityGroupViewModel = hiltViewModel(),
    onFinish: () -> Unit
) {
    val uiState: NewAvailabilityGroupUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(uiState) {
        if (uiState.isCompleted) {
            Toast.makeText(context, "Availabilities created successfully", Toast.LENGTH_LONG).show()
            onFinish()
        }
    }
    NewAvailabilityGroupScreen(
        uiState,
        onChangeEndDate = viewModel::onSelectEndDate,
        onChangeStartDate = viewModel::onSelectStartDate,
        onAddNewAvailability = viewModel::addAvailability,
        onClickDelete = viewModel::delete,
        onClickOk = viewModel::createAvailabilities
    )
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewAvailabilityGroupScreen(
    uiState: NewAvailabilityGroupUiState,
    onChangeStartDate: (LocalDate) -> Unit,
    onChangeEndDate: (LocalDate) -> Unit,
    onAddNewAvailability: (String, TimeInterval) -> Unit,
    onClickDelete: (String, TimeInterval) -> Unit,
    onClickOk: (Long) -> Unit,
) {

    var isTimeIntervalPickerShown by remember {
        mutableStateOf(false)
    }
    var keyDayForAdd by remember {
        mutableStateOf("")
    }
    val startTimeState = rememberTimePickerState(
        is24Hour = true
    )
    val endTimeState = rememberTimePickerState(
        is24Hour = true
    )

    var errorMessage by remember {
        mutableStateOf("")
    }

    val openDialogStartDate = remember { mutableStateOf(false) }
    val openDialogEndDate = remember { mutableStateOf(false) }

    val startDatePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                val startOfDayMillis = calendar.timeInMillis
                return utcTimeMillis >= startOfDayMillis
            }
        }
    )
    val endDatePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                val startOfDayMillis = calendar.timeInMillis
                return utcTimeMillis >= startOfDayMillis
            }
        }
    )

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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Box(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .background(Color(0xFF3F5AA6))
            ) {
                Text(
                    text = "Add New Availability",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        if (!isTimeIntervalPickerShown) {
            item {
                Spacer(modifier = Modifier.size(20.dp))
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
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
                                    color = Color(0xFFA68B3F),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    openDialogStartDate.value = true
                                }
                        ) {
                            Text(
                                text = formatDate(uiState.startDate, "dd/MM/yyyy"),
                                modifier = Modifier.align(Alignment.Center)
                            )
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
                                    color = Color(0xFFA68B3F),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    openDialogEndDate.value = true
                                }
                        ) {
                            Text(
                                text = formatDate(uiState.endDate, "dd/MM/yyyy"),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.size(20.dp))
            }
            item {
                HeaderDay("sunday") {
                    keyDayForAdd = "sunday"
                    isTimeIntervalPickerShown = true
                }
            }
            if (uiState.days.get("sunday")!!.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        uiState.days.get("sunday")!!.forEach {
                            Spacer(modifier = Modifier.size(10.dp))
                            AvailabilityItem(model = it) {
                                onClickDelete.invoke(keyDayForAdd, it)
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.size(20.dp))
            }
            item {
                HeaderDay("monday") {
                    keyDayForAdd = "monday"
                    isTimeIntervalPickerShown = true
                }
            }
            if (uiState.days.get("monday")!!.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        uiState.days.get("monday")!!.forEach {
                            Spacer(modifier = Modifier.size(10.dp))
                            AvailabilityItem(model = it) {
                                onClickDelete.invoke(keyDayForAdd, it)
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.size(20.dp))
            }
            item {
                HeaderDay("tuesday") {
                    keyDayForAdd = "tuesday"
                    isTimeIntervalPickerShown = true
                }
            }
            if (uiState.days.get("tuesday")!!.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        uiState.days.get("tuesday")!!.forEach {
                            Spacer(modifier = Modifier.size(10.dp))
                            AvailabilityItem(model = it) {
                                onClickDelete.invoke(keyDayForAdd, it)
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.size(20.dp))
            }
            item {
                HeaderDay("wednesday") {
                    keyDayForAdd = "wednesday"
                    isTimeIntervalPickerShown = true
                }
            }
            if (uiState.days.get("wednesday")!!.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        uiState.days.get("wednesday")!!.forEach {
                            Spacer(modifier = Modifier.size(10.dp))
                            AvailabilityItem(model = it) {
                                onClickDelete.invoke(keyDayForAdd, it)
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.size(20.dp))
            }
            item {
                HeaderDay("thursday") {
                    keyDayForAdd = "thursday"
                    isTimeIntervalPickerShown = true
                }
            }
            if (uiState.days.get("thursday")!!.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        uiState.days.get("thursday")!!.forEach {
                            Spacer(modifier = Modifier.size(10.dp))
                            AvailabilityItem(model = it) {
                                onClickDelete.invoke(keyDayForAdd, it)
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.size(20.dp))
            }
            item {
                HeaderDay("friday") {
                    keyDayForAdd = "friday"
                    isTimeIntervalPickerShown = true
                }
            }
            if (uiState.days.get("friday")!!.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        uiState.days.get("friday")!!.forEach {
                            Spacer(modifier = Modifier.size(10.dp))
                            AvailabilityItem(model = it) {
                                onClickDelete.invoke(keyDayForAdd, it)
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.size(20.dp))
            }
            item {
                HeaderDay("saturday") {
                    keyDayForAdd = "saturday"
                    isTimeIntervalPickerShown = true
                }
            }
            if (uiState.days.get("saturday")!!.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        uiState.days.get("saturday")!!.forEach {
                            Spacer(modifier = Modifier.size(10.dp))
                            AvailabilityItem(model = it) {
                                onClickDelete.invoke(keyDayForAdd, it)
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.size(20.dp))
            }
            item {
                Text(
                    text = uiState.errorMessage ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.Red
                )
            }
            item {
                val context = LocalContext.current
                Button(
                    onClick = {
                        val sharedPref =
                            context.getSharedPreferences("project-graduation", Context.MODE_PRIVATE)
                        val psychologistId = sharedPref.getLong("psychologist_id", 0)
                        onClickOk.invoke(psychologistId)
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA68B3F)
                    ),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(text = "Submit")
                }
            }
        } else {
            item {
                Spacer(modifier = Modifier.size(50.dp))
            }
            item {
                TimeInput(state = startTimeState)
            }
            item {
                Spacer(modifier = Modifier.size(20.dp))
            }
            item {
                TimeInput(state = endTimeState)
            }
            item {
                Spacer(modifier = Modifier.size(20.dp))
            }
            item {
                Button(
                    onClick = {
                        fun isIntervalMultipleOf30Minutes(interval: TimeInterval): Boolean {
                            val startMinutes = convertToMinutes(interval.start)
                            val endMinutes = convertToMinutes(interval.end)
                            val durationMinutes = endMinutes - startMinutes

                            return durationMinutes % 30 == 0
                        }

                        // Fonction pour convertir le temps au format "HH:mm" en minutes
                        fun convertToMinutes(time: String): Int {
                            val parts = time.split(":")
                            val hours = parts[0].toInt()
                            val minutes = parts[1].toInt()
                            return hours * 60 + minutes
                        }

                        fun isStartTimeBeforeEndTime(
                            startTime: TimePickerState,
                            endTime: TimePickerState
                        ): Boolean {
                            return if (startTime.hour < endTime.hour) {
                                true
                            } else if (startTime.hour == endTime.hour && startTime.minute < endTime.minute) {
                                true
                            } else {
                                false
                            }
                        }

                        fun formatTime(hours: Int, minutes: Int): String {
                            return String.format("%02d:%02d", hours, minutes)
                        }
                        errorMessage = ""
                        val isStartTimeBeforeEndTime =
                            isStartTimeBeforeEndTime(startTimeState, endTimeState)
                        if (isStartTimeBeforeEndTime) {
                            val timeInterval = TimeInterval(
                                start = formatTime(startTimeState.hour, startTimeState.minute),
                                end = formatTime(endTimeState.hour, endTimeState.minute)
                            )
                            val isOverLap = isOverlap(timeInterval, uiState.days[keyDayForAdd]!!)
                            if (!isOverLap) {
                                val intervalValid = isIntervalMultipleOf30Minutes(timeInterval)
                                if (intervalValid) {
                                    onAddNewAvailability.invoke(keyDayForAdd, timeInterval)
                                    isTimeIntervalPickerShown = false
                                } else {
                                    errorMessage =
                                        "Invalid interval duration. The selected interval is not a multiple of 30 minutes"
                                }
                            } else {
                                errorMessage =
                                    "Overlap detected! The selected interval overlaps with another existing interval."
                            }
                        } else {
                            errorMessage = "Oops! The start time you selected is after the end time"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA68B3F)
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(text = "Add")
                }
                Spacer(modifier = Modifier.size(20.dp))
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    text = errorMessage,
                    color = Color.Red
                )
            }
            item {
                // Text(text = "",color)
            }
        }
    }
}

@Composable
fun HeaderDay(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(40.dp)
            .background(Color(0xFF5873C0), RoundedCornerShape(6.dp))
            .clickable {
                onClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.size(20.dp))
        Text(text = text, color = Color.White, modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Filled.Add, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.size(20.dp))
    }
}

fun convertLongToLocalDate(dateLong: Long): LocalDate {
    return Instant.ofEpochMilli(dateLong)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun CustomDatePicker(
    onDismissRequest: () -> Unit,
    onSubmit: (LocalDate) -> Unit,
    datePickerState: DatePickerState
) {
    val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    val date = convertLongToLocalDate(datePickerState.selectedDateMillis!!)
                    onSubmit(date)
                },
                enabled = confirmEnabled.value
            ) {
                Text("OK", color = Color(0xFFA68B3F))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("Cancel", color = Color(0xFFA68B3F))
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                selectedDayContainerColor = Color(0xFFA68B3F)
            )
        )
    }
}

fun formatDate(localDate: LocalDate?, pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return localDate?.format(formatter) ?: ""
}

@Composable
fun AvailabilityItem(model: TimeInterval, onClickDelete: (TimeInterval) -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xffd2c59f)
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.size(10.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = model.start +
                            " -> " + model.end, color = Color(0xFF3F5AA6)
                )
            }
            Icon(
                imageVector = Icons.Filled.Delete,
                tint = Color.Red,
                contentDescription = null,
                modifier = Modifier.clickable {
                    onClickDelete.invoke(model)
                }
            )
            Spacer(modifier = Modifier.size(10.dp))
        }
    }
}

fun isOverlap(interval: TimeInterval, intervalList: List<TimeInterval>): Boolean {
    for (existingInterval in intervalList) {
        val existingStart = existingInterval.start
        val existingEnd = existingInterval.end

        // Convertir les temps en minutes pour faciliter la comparaison
        val intervalStartMinutes = convertToMinutes(interval.start)
        val intervalEndMinutes = convertToMinutes(interval.end)
        val existingStartMinutes = convertToMinutes(existingStart)
        val existingEndMinutes = convertToMinutes(existingEnd)

        // Vérifier si le début de l'intervalle existant est strictement supérieur au début de l'intervalle donné,
        // et si la fin de l'intervalle existant est strictement inférieure à la fin de l'intervalle donné.
        // Si c'est le cas, les intervalles se chevauchent réellement.
        if ((intervalStartMinutes < existingStartMinutes && existingStartMinutes < intervalEndMinutes) ||
            (intervalStartMinutes < existingEndMinutes && existingEndMinutes < intervalEndMinutes)
        ) {
            return true
        }
    }

    return false
}


// Fonction pour convertir le temps au format "HH:mm" en minutes
fun convertToMinutes(time: String): Int {
    val parts = time.split(":")
    val hours = parts[0].toInt()
    val minutes = parts[1].toInt()
    return hours * 60 + minutes
}
