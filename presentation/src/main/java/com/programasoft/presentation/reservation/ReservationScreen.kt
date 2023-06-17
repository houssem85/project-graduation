package com.programasoft.presentation.reservation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.programasoft.presentation.utils.roboto
import io.github.farhanroy.composeawesomedialog.SuccessHeader
import io.github.farhanroy.composeawesomedialog.components.OkButton

@Composable
fun ReservationRoute(
    viewModel: ReservationViewModel = hiltViewModel(),
    onReservationSuccess: () -> Unit,
    onBackClicked: () -> Unit,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isReservationSuccess) {
        CustomSuccessDialog(
            "Success!",
            "Success! Reservation complete. Your reservation will be confirmed definitively upon making the payment in the 'Reservations' menu.",
            {
                onReservationSuccess()
            })
    }

    ReservationScreen(
        reservationUiState = uiState,
        onSelectDate = viewModel::selectDate,
        onValidateClick = {
            val sharedPref =
                context.getSharedPreferences("project-graduation", Context.MODE_PRIVATE)
            val clientId = sharedPref.getLong("client_id", 0)
            viewModel.validate(clientId)
        },
        onBackClicked = onBackClicked
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationScreen(
    reservationUiState: ReservationUiState,
    onSelectDate: (Long) -> Unit = {},
    onValidateClick: () -> Unit,
    onBackClicked: () -> Unit,
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
                    Box(
                        modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth()
                            .background(Color(0xFF3F5AA6))
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBackIos,
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 10.dp)
                                .clickable {
                                    onBackClicked.invoke()
                                },
                            tint = Color.White
                        )
                        Text(
                            text = "New Reservation",
                            color = Color.White,
                            fontFamily = roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                item {
                    val datePickerState = rememberDatePickerState(
                        selectableDates = object : SelectableDates {
                            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                                return reservationUiState.availableDates.contains(utcTimeMillis)
                            }
                        }
                    )
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
                        colors = DatePickerDefaults.colors(
                            selectedDayContainerColor = Color(0xFFA68B3F),
                            todayDateBorderColor = Color(0xFFA68B3F)
                        )
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
                            shape = RoundedCornerShape(6.dp),
                            onClick = {
                                availabilityUnit.onClick.invoke()
                            },
                            label = {
                                Text(
                                    textAlign = TextAlign.Center,
                                    fontSize = 16.sp,
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
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = reservationUiState.errorMessage,
                        fontFamily = roboto,
                        fontSize = 16.sp,
                        color = Color.Red,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFA68B3F),
                                disabledContainerColor = Color(0xFFA68B3F).copy(0.5f)
                            ),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 20.dp),
                            enabled = test,
                            onClick = onValidateClick
                        ) {
                            Text(
                                text = "Validate",
                                fontSize = 18.sp,
                                fontFamily = roboto,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(20.dp))
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

@Composable
fun CustomSuccessDialog(
    title: String = "",
    desc: String = "",
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            Modifier
                .width(300.dp)
                .height(270.dp)
        ) {
            Column(
                Modifier
                    .width(300.dp)
                    .height(270.dp)
            ) {
                Spacer(Modifier.height(36.dp))
                Box(
                    Modifier
                        .width(300.dp)
                        .height(270.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            title.uppercase(),
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            desc,
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontSize = 16.sp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OkButton(
                                Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .clickable {
                                        onDismiss.invoke()
                                    }
                            )
                        }
                    }
                }
            }
            SuccessHeader(
                Modifier
                    .size(72.dp)
                    .align(Alignment.TopCenter)
                    .border(
                        border = BorderStroke(width = 5.dp, color = Color.White),
                        shape = CircleShape
                    )
            )
        }
    }
}