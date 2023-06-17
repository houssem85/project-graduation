package com.programasoft.presentation.video

import android.os.Build
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
import io.agora.rtc.Constants

@Composable
fun VideoRoute(
    viewModel: VideoViewModel = hiltViewModel(),
    onClickBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    VideoScreen(
        uiState,
        onClickBack
    )
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VideoScreen(
    uiState: VideoUiState,
    onClickBack: () -> Unit
) {

    val permissionsState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        rememberMultiplePermissionsState(
            listOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.BLUETOOTH_CONNECT
            ),
        )
    } else {
        rememberMultiplePermissionsState(
            listOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.RECORD_AUDIO
            ),
        )
    }

    LaunchedEffect(Unit) {
        permissionsState.launchMultiplePermissionRequest()
    }

    if (permissionsState.permissions.first().status.isGranted) {
        if (uiState.remainingTimeInMinutes != 0L && uiState.reservationId != 0L) {
            var agoraViewer: AgoraVideoViewer? = null

            LaunchedEffect(uiState.remainingTimeInMinutes) {
                if (uiState.remainingTimeInMinutes <= 0L) {
                    onClickBack.invoke()
                }
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
                                onClickBack.invoke()
                            },
                        tint = Color.White
                    )
                    Text(
                        text = convertMinutesToHHMM(uiState.remainingTimeInMinutes),
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
                        it.join(
                            uiState.reservationId.toString(),
                            role = Constants.CLIENT_ROLE_BROADCASTER
                        )
                    }
                }, update = {
                    agoraViewer = it
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
fun convertMinutesToHHMM(minutes: Long): String {
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return String.format("%02d:%02d", hours, remainingMinutes)
}