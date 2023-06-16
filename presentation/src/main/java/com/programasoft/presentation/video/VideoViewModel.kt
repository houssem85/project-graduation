package com.programasoft.presentation.video

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programasoft.data.network.NetworkApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    val networkApi: NetworkApi,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState: MutableStateFlow<VideoUiState> = MutableStateFlow(VideoUiState())
    val uiState = _uiState.asStateFlow()

    private val reservationId: Long = checkNotNull(savedStateHandle["reservationId"])

    init {
        viewModelScope.launch {
            try {
                val res = networkApi.getReservationEndTime(reservationId)
                if (res.code() == 200) {
                    _uiState.update {
                        VideoUiState(
                            endTime = res.body()!!,
                            reservationId = reservationId
                        )
                    }
                }
            } catch (ex: Exception) {
                _uiState.update {
                    VideoUiState(
                        endTime = 0,
                        reservationId = reservationId
                    )
                }
            }
        }
    }
}

data class VideoUiState(
    val endTime: Long = 0,
    val reservationId: Long = 0,
)