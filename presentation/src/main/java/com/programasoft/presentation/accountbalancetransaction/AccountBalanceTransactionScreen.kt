@file:OptIn(ExperimentalMaterial3Api::class)

package com.programasoft.presentation.accountbalancetransaction

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.programasoft.data.network.model.TransactionResponse
import java.text.DecimalFormat


@Composable
fun AccountBalanceTransactionRoute(
    viewModel: AccountBalanceTransactionViewModel = hiltViewModel(),
    onEnterAmount: (Double) -> Unit
) {
    val uiState: AccountBalanceTransactionUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(
        Unit
    ) {
        val sharedPref = context.getSharedPreferences("project-graduation", Context.MODE_PRIVATE)
        val accountId = sharedPref.getLong("account_id", 0)
        viewModel.loadData(accountId)
    }

    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value)
        CustomDialog(value = "", setShowDialog = {
            showDialog.value = it
        }, onEnterAmount)

    AccountBalanceTransactionScreen(
        uiState,
        onClickAddCredit = {
            showDialog.value = true
        }
    )
}

@Composable
fun AccountBalanceTransactionScreen(
    uiState: AccountBalanceTransactionUiState,
    onClickAddCredit: () -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)

    ) {
        if (!uiState.loading) {
            val header = createRef()
            Column(
                modifier = Modifier
                    .background(
                        Color(0xFF3F5AA6)
                    )
                    .fillMaxWidth()
                    .padding(20.dp)
                    .constrainAs(header) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Text(
                    text = priceFormat(uiState.balance),
                    color = Color.White,
                    fontSize = 30.sp
                )
                Text(
                    text = "Account Balance",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
            val addButton = createRef()
            IconButton(colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = Color(0xFFA68B3F)
            ), onClick = onClickAddCredit, modifier = Modifier.constrainAs(addButton) {
                top.linkTo(header.bottom)
                bottom.linkTo(header.bottom)
                end.linkTo(parent.end, margin = 20.dp)
            }) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            val negativePositiveSection = createRef()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(negativePositiveSection) {
                        top.linkTo(header.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = priceFormat(uiState.creditBalance),
                        color = Color.Black,
                        fontSize = 24.sp
                    )
                    Text(
                        text = "Credits Balance",
                        color = Color(0xFF3F5AA6),
                        fontSize = 18.sp
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = priceFormat(uiState.debitBalance),
                        color = Color.Black,
                        fontSize = 24.sp
                    )
                    Text(
                        text = "Debits Balance",
                        color = Color(0xFF3F5AA6),
                        fontSize = 18.sp
                    )
                }
            }
            val historyTitle = createRef()
            Box(modifier = Modifier
                .height(40.dp)
                .background(Color(0xFFAEAEAE))
                .constrainAs(historyTitle) {
                    top.linkTo(negativePositiveSection.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }) {
                Text(
                    textAlign = TextAlign.Center,
                    text = "History", modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 20.dp),
                    color = Color.White
                )
            }
            val list = createRef()
            LazyColumn(
                contentPadding = PaddingValues(top = 20.dp, end = 10.dp, start = 10.dp),
                modifier = Modifier.constrainAs(list) {
                    top.linkTo(historyTitle.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                },
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.items) {
                    ListItem(model = it)
                }
            }

        } else {
            val loader = createRef()
            CircularProgressIndicator(
                modifier = Modifier.constrainAs(loader) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}

@Composable
fun priceFormat(value: Double): String {
    val df = DecimalFormat("#,###.##")
    val roundoff = df.format(value)
    return roundoff + " DT"
}

@Composable
fun CustomDialog(value: String, setShowDialog: (Boolean) -> Unit, setValue: (Double) -> Unit) {

    val txtFieldError = remember { mutableStateOf("") }
    val txtField = remember { mutableStateOf(value) }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Enter Amount",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "",
                            tint = colorResource(android.R.color.darker_gray),
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { setShowDialog(false) }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                BorderStroke(
                                    width = 2.dp,
                                    color = if (txtFieldError.value.isEmpty()) Color(0xFF3F5AA6) else Color.Red
                                ),
                                shape = RoundedCornerShape(6.dp)
                            ),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Money,
                                contentDescription = "",
                                tint = Color(0xFF3F5AA6),
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)
                            )
                        },
                        placeholder = { Text(text = "Amount", color = Color.Gray) },
                        value = txtField.value,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = {
                            txtField.value = it.take(10)
                        })

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFA68B3F)
                            ),
                            onClick = {
                                if (txtField.value.isEmpty()) {
                                    txtFieldError.value = "Field can not be empty"
                                    return@Button
                                }
                                val amount = txtField.value.toDoubleOrNull()
                                if (amount == null) {
                                    txtFieldError.value = "Field is not number"
                                    return@Button
                                } else {
                                    if (amount <= 0.0) {
                                        txtFieldError.value = "Field can not be negative"
                                        return@Button
                                    }
                                }
                                setValue(amount)
                                setShowDialog(false)
                            },
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "OK")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListItem(model: TransactionResponse) {
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
                Text(text = model.type.lowercase().capitalize(), color = Color(0xFF3F5AA6))
                Text(text = model.date.replace("T", " ").replace("+00:00", ""), color = Color.Black)
            }
            Text(
                text = priceFormat(value = model.amount.toDouble()),
                fontWeight = FontWeight.Bold,
                color = if (model.type == "DEPOSIT") Color(0xFF007500) else Color(0xFFD10000)
            )
            Spacer(modifier = Modifier.size(10.dp))
        }
    }
}