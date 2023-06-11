package com.programasoft.presentation.availabilities

import android.content.Context
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.programasoft.data.network.model.AvailabilityGroup
import com.programasoft.presentation.newavailabilitygroup.TimeInterval

@Composable
fun AvailabilitiesRoute(
    viewModel: AvailabilitiesViewModel = hiltViewModel(),
    onAddClicked: () -> Unit
) {
    val uiState: AvailabilitiesUiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val sharedPref = context.getSharedPreferences("project-graduation", Context.MODE_PRIVATE)
        val psychologistId = sharedPref.getLong("psychologist_id", 0)
        viewModel.getData(psychologistId)
    }

    AvailabilitiesScreen(
        uiState = uiState,
        onAddClicked = onAddClicked
    )
}

@Composable
fun AvailabilitiesScreen(
    uiState: AvailabilitiesUiState, onAddClicked: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .background(Color(0xFF3F5AA6))
            ) {
                Text(
                    text = "Availabilities",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            LazyColumn(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(uiState.data) {
                    AvailabilityGroupItem(it)
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(14.dp),
            onClick = {
                onAddClicked.invoke()
            },
            containerColor = Color(0xFFA68B3F),
            shape = RoundedCornerShape(16.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Add FAB",
                tint = Color.White,
            )
        }
    }
}

@Composable
fun AvailabilityGroupItem(model: AvailabilityGroup) {
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
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.size(10.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = model.startDate + " -> " + model.endDate, color = Color(0xFF3F5AA6)
                )
            }
            Icon(
                imageVector = Icons.Filled.Delete,
                tint = Color.Red,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(10.dp))
        }
    }
}