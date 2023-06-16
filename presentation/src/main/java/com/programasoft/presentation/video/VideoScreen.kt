package com.programasoft.presentation.video

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import io.agora.agorauikit_android.AgoraConnectionData
import io.agora.agorauikit_android.AgoraVideoViewer

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
            AndroidView(factory = {
                AgoraVideoViewer(
                    it, connectionData = AgoraConnectionData(
                        appId = "2b74cb782dd140b28b382735aa8aa586"
                    )
                ).also {
                    agoraViewer = it
                    it.join(uiState.reservationId.toString())
                }
            }, modifier = Modifier.fillMaxSize())
            BackHandler {
                agoraViewer?.leaveChannel()
            }
        }
    }
}