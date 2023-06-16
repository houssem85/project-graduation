package com.programasoft.presentation.video

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import io.agora.agorauikit_android.AgoraConnectionData
import io.agora.agorauikit_android.AgoraSettings
import io.agora.agorauikit_android.AgoraVideoViewer
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.TimeUnit

@Composable
fun VideoRoute(
    viewModel: VideoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    VideoScreen(
        uiState
    )
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VideoScreen(
    uiState: VideoUiState
) {

    val permissionsState = rememberMultiplePermissionsState(
        listOf(android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO)
    )

    LaunchedEffect(Unit) {
        permissionsState.launchMultiplePermissionRequest()
    }

    if (permissionsState.permissions.first().status.isGranted) {
        if (uiState.endTime != 0L && uiState.reservationId != 0L) {
            var agoraViewer: AgoraVideoViewer? = null
            var currentTime: Long by remember {
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                mutableStateOf(calendar.timeInMillis)
            }
            LaunchedEffect(Unit) {
                while (true) {
                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    currentTime = calendar.timeInMillis
                    delay(1000)
                }
            }
            var time: Long by remember(currentTime) {
                val value = uiState.endTime - currentTime
                mutableStateOf(value)
            }
            Column(modifier = Modifier.fillMaxSize()) {
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
                                agoraViewer?.leaveChannel()
                            },
                        tint = Color.White
                    )
                    Text(
                        text = convertMillisToTime(time),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                AndroidView(factory = {
                    val settings = AgoraSettings()
                    settings.enabledButtons = mutableSetOf(AgoraSettings.BuiltinButton.MIC)
                    AgoraVideoViewer(
                        it, connectionData = AgoraConnectionData(
                            appId = "2b74cb782dd140b28b382735aa8aa586"
                        ), agoraSettings = settings
                    ).also {
                        agoraViewer = it
                        it.join(uiState.reservationId.toString())
                    }
                }, modifier = Modifier.weight(1f))
            }
            BackHandler {
                agoraViewer?.leaveChannel()
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }
        }
    }
}

@Composable
fun convertMillisToTime(milliseconds: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}