package com.programasoft.presentation.newavailabilitygroup

import androidx.lifecycle.ViewModel
import com.programasoft.data.network.NetworkApi
import com.programasoft.presentation.reservation.ReservationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NewAvailabilityGroupViewModel @Inject constructor(
    private val networkApi: NetworkApi
) : ViewModel() {

    private val _uiState: MutableStateFlow<NewAvailabilityGroupUiState> = MutableStateFlow(
        NewAvailabilityGroupUiState()
    )
    val uiState = _uiState.asStateFlow()

    fun onSelectStartDate(date: String) {
        _uiState.update {
            it.copy(startDate = date)
        }
    }

    fun onSelectEndDate(date: String) {
        _uiState.update {
            it.copy(endDate = date)
        }
    }
}

data class NewAvailabilityGroupUiState(
    val startDate: String = "",
    val endDate: String = ""
)