package com.programasoft.presentation.psychologistprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.BookOnline
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.programasoft.presentation.R
import com.programasoft.presentation.psychologists.toBitmap


@Composable
fun PsychologistProfileRoute(
    viewModel: PsychologistProfileViewModel = hiltViewModel(),
    onClickReservation: (Int) -> Unit,
    onBackClicked: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PsychologistProfileScreen(
        psychologistProfileUiState = uiState,
        onClickReservation = onClickReservation,
        onBackClicked = onBackClicked
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PsychologistProfileScreen(
    psychologistProfileUiState: PsychologistProfileUiState,
    onClickReservation: (Int) -> Unit,
    onBackClicked: () -> Unit,
) {

    val avenir = FontFamily(
        Font(R.font.avenir, FontWeight.Normal),
        Font(R.font.avenir, FontWeight.Light),
        Font(R.font.avenir, FontWeight.SemiBold),
        Font(R.font.avenir, FontWeight.ExtraBold)
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(Color.White)
                .fillMaxSize()
                .padding(
                    top = 60.dp, start = 20.dp, end = 20.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            psychologistProfileUiState.psychologist?.let {
                Spacer(modifier = Modifier.size(20.dp))
                if (psychologistProfileUiState.psychologist.image.toBitmap() != null) {
                    Image(
                        modifier = Modifier.size(150.dp),
                        bitmap = it.image.toBitmap()!!.asImageBitmap(),
                        contentDescription = null
                    )
                } else {
                    Image(
                        modifier = Modifier.size(60.dp),
                        imageVector = Icons.Rounded.AccountCircle,
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.size(20.dp))
                Text(
                    text = it.account.fullName,
                    fontFamily = avenir,
                    fontSize = 26.sp,
                    color = Color(
                        0XFF3F5AA6
                    )
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = it.fullAddress,
                    fontFamily = avenir,
                    color = Color(
                        0xFF828899
                    ),
                    maxLines = 1,
                    fontSize = 13.sp,
                    lineHeight = 16.sp,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.size(20.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start),
                    text = "DOMAINS",
                    fontFamily = avenir,
                    fontSize = 14.sp,
                    color = Color(
                        0XFF3F5AA6
                    )
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    it.domains.forEach {
                        AssistChip(
                            onClick = { /* Do something! */ },
                            label = { Text(it.displayName) },
                            leadingIcon = {

                            },
                            colors = AssistChipDefaults.assistChipColors(
                                labelColor = Color.Black, containerColor = Color.White
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start),
                    text = "HOURLY RATE",
                    fontFamily = avenir,
                    fontSize = 14.sp,
                    color = Color(
                        0XFF3F5AA6
                    )
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = "${it.hourlyRate} DT per hour",
                    fontFamily = avenir,
                    color = Color(
                        0xFF828899
                    ),
                    maxLines = 1,
                    fontSize = 13.sp,
                    lineHeight = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.size(20.dp))
                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(
                            Color(
                                0XFF3F5AA6
                            )
                        )
                )
                Spacer(modifier = Modifier.size(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = { }) {
                            Icon(
                                modifier = Modifier.size(80.dp),
                                imageVector = Icons.Outlined.Send,
                                contentDescription = null,
                                tint = Color(
                                    0XFF3F5AA6
                                )
                            )
                        }
                        Text(
                            "send message", color = Color(
                                0XFF3F5AA6
                            ), fontFamily = avenir, fontSize = 12.sp
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(50.dp)
                            .width(1.dp)
                            .background(
                                Color(
                                    0XFF3F5AA6
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                            .clickable {
                                onClickReservation(psychologistProfileUiState.psychologist.id.toInt())
                            }, horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = {
                            onClickReservation(psychologistProfileUiState.psychologist.id.toInt())
                        }) {
                            Icon(
                                modifier = Modifier.size(80.dp),
                                imageVector = Icons.Outlined.BookOnline,
                                contentDescription = null,
                                tint = Color(
                                    0XFF3F5AA6
                                )
                            )
                        }
                        Text(
                            "Reservation", color = Color(
                                0XFF3F5AA6
                            ), fontFamily = avenir, fontSize = 12.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.size(6.dp))
                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(
                            Color(
                                0XFF3F5AA6
                            )
                        )
                )
                Spacer(modifier = Modifier.size(20.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start),
                    text = "PRESENTATION",
                    fontFamily = avenir,
                    fontSize = 14.sp,
                    color = Color(
                        0XFF3F5AA6
                    )
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start),
                    text = it.presentation,
                    fontFamily = avenir,
                    fontSize = 16.sp,
                    color = Color(
                        0XFF828899
                    )
                )
            }
        }
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
                text = "Psychologist Profile",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}