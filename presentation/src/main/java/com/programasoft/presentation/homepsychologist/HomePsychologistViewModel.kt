package com.programasoft.presentation.homepsychologist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programasoft.data.network.NetworkApi
import com.programasoft.data.network.model.ReservationReadyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePsychologistViewModel @Inject constructor(
    private val networkApi: NetworkApi
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomePsychologistUiState())
    val uiState: StateFlow<HomePsychologistUiState> = _uiState

    fun loadData(psychologistId: Long) {
        viewModelScope.launch {
            while (true) {
                try {
                    val res = networkApi.getReadyReservationsByPsychologist(psychologistId)
                    if (res.code() == 200) {
                        _uiState.update {
                            it.copy(
                                items = res.body()!!
                            )
                        }
                    }
                } catch (ex: Exception) {

                }
                delay(20000)
            }
        }
    }
}

data class HomePsychologistUiState(
    val items: List<ReservationReadyResponse> = emptyList()
)