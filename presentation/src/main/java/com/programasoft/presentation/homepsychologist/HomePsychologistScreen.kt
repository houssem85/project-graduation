package com.programasoft.presentation.homepsychologist

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.programasoft.data.network.model.ReservationReadyResponse


@Composable
fun HomePsychologistRoute(
    viewModel: HomePsychologistViewModel = hiltViewModel(),
    onJoinConsultation: (Long) -> Unit,
    onBackClicked: () -> Unit,
    onLogOutClicked: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val sharedPref = context.getSharedPreferences("project-graduation", Context.MODE_PRIVATE)
        val psychologistId = sharedPref.getLong("psychologist_id", 0)
        viewModel.loadData(psychologistId)
    }

    HomePsychologistScreen(
        uiState, onJoinConsultation, onBackClicked, onLogOutClicked
    )
}

@Composable
fun HomePsychologistScreen(
    uiState: HomePsychologistUiState,
    onJoinConsultation: (Long) -> Unit,
    onBackClicked: () -> Unit,
    onLogOutClicked: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
                .background(Color(0xFF3F5AA6))
        ) {
            val context = LocalContext.current
            Icon(
                imageVector = Icons.Outlined.Logout,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp)
                    .clickable {
                        val sharedPref =
                            context.getSharedPreferences("project-graduation", Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = sharedPref.edit()
                        editor.clear()
                        editor.apply()
                        onLogOutClicked.invoke()
                    },
                tint = Color.White
            )
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
                text = "Today's Reservations",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(uiState.items) {
                ListItem(
                    it
                ) {
                    onJoinConsultation.invoke(it)
                }
            }
        }
    }
}

@Composable
fun ListItem(model: ReservationReadyResponse, onJoinClick: (Long) -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth(),
        border = BorderStroke(
            color = Color(0xFF3F8EA6), width = 1.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.size(10.dp))
            Image(
                modifier = Modifier.size(40.dp),
                imageVector = Icons.Filled.VideoCall,
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = Color(0xFF3F8EA6))
            )
            Spacer(modifier = Modifier.size(10.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = model.client, color = Color(0xFF3F5AA6))
                Text(
                    text = model.startTime + " -> " + model.endTime, color = Color.Black
                )
            }
            if (model.isReadyForJoin) {
                Column {
                    Button(
                        onClick = {
                            onJoinClick.invoke(model.id)
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ), shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Join", fontWeight = FontWeight.Bold, color = Color(0xFF3F5AA6)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
        }
    }
}