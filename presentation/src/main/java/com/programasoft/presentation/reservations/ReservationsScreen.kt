package com.programasoft.presentation.reservations

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.programasoft.data.network.model.ReservationResponse
import com.programasoft.presentation.accountbalancetransaction.priceFormat
import com.programasoft.presentation.utils.roboto

@Composable
fun ReservationsRoute(
    viewModel: ReservationsViewModel = hiltViewModel()
) {

    val uiState: ReservationsUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState) {
        if (uiState.errorMessage.isNotEmpty()) {
            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_LONG).show()
            viewModel.errorMessageShown()
        }
    }
    var state by remember { mutableStateOf(0) }

    LaunchedEffect(state) {
        val sharedPref = context.getSharedPreferences("project-graduation", Context.MODE_PRIVATE)
        val accountId = sharedPref.getLong("account_id", 0)
        val clientId = sharedPref.getLong("client_id", 0)
        viewModel.getData(accountId, clientId, state != 0)
    }

    ReservationScreen(uiState, state, {
        state = it
    }, {
        val sharedPref = context.getSharedPreferences("project-graduation", Context.MODE_PRIVATE)
        val accountId = sharedPref.getLong("account_id", 0)
        val clientId = sharedPref.getLong("client_id", 0)
        viewModel.onPaymentClick(it, accountId, clientId, state != 0)
    })
}

@Composable
fun ReservationScreen(
    uiState: ReservationsUiState,
    state: Int,
    onChangeState: (Int) -> Unit,
    onPaymentClick: (Long) -> Unit
) {
    val titles = listOf(
        Pair("Unconfirmed", Icons.Filled.MoneyOff),
        Pair("Confirmed", Icons.Filled.AttachMoney)
    )
    Column(
        modifier = Modifier.background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
                .background(Color(0xFF3F5AA6))
        ) {
            Text(
                text = "Reservations",
                color = Color.White,
                fontFamily = roboto,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        TabRow(
            modifier = Modifier.padding(bottom = 10.dp), selectedTabIndex = state,
            divider = {

            },
            indicator = @Composable { tabPositions ->
                if (state < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[state]),
                        color = Color(0xFF3F5AA6)
                    )
                }
            }
        ) {
            titles.forEachIndexed { index, pair ->
                Tab(
                    selectedContentColor = Color(0xFF3F5AA6),
                    unselectedContentColor = Color(0xFF3F5AA6),
                    selected = state == index,
                    onClick = { onChangeState.invoke(index) },
                    icon = {
                        Icon(imageVector = pair.second, contentDescription = "")
                    },
                    text = {
                        Text(
                            fontSize = 13.sp,
                            fontFamily = roboto,
                            text = pair.first,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp)
        ) {
            items(uiState.reservations) {
                ListItem(it, onPaymentClick)
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(40.dp)
                .background(Color(0xFF3F8EA6))
        ) {
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = "Account Balance",
                color = Color.White,
                fontFamily = roboto,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = priceFormat(uiState.balance),
                color = Color.White,
                fontFamily = roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.size(10.dp))
        }
    }
}

@Composable
fun ListItem(model: ReservationResponse, onPaymentClick: (Long) -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFA68B3F)
        ),
        modifier = Modifier
            .height(90.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .width(70.dp)
                    .height(90.dp)
                    .background(
                        color = Color(0xFFA68B3F),
                        shape = RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp)
                    )
            ) {
                Text(
                    color = Color.White,
                    fontFamily = roboto,
                    fontWeight = FontWeight.Bold,
                    text = priceFormat(value = model.amount.toDouble()), modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = model.psychologist, color = Color(0xFF3F5AA6))
                Text(
                    text = model.date,
                    color = Color.Black
                )
                Text(
                    text = model.startTime + " -> " + model.endTime,
                    color = Color.Black
                )
            }
            if (!model.isPaid) {
                Column {
                    Button(
                        onClick = {
                            onPaymentClick.invoke(model.id)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Pay Now",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3F5AA6)
                        )
                    }
                }
            }
        }
    }
}