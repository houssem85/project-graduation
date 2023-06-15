package com.programasoft.presentation.join

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programasoft.data.network.NetworkApi
import com.programasoft.data.network.model.ReservationReadyResponse
import com.programasoft.presentation.login.LoginClientUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val networkApi: NetworkApi,
) : ViewModel() {

    private val _uiState = MutableStateFlow(JoinUiState())
    val uiState: StateFlow<JoinUiState> = _uiState

    fun loadData(clientId: Long) {
        viewModelScope.launch {
            while (true) {
                try {
                    val res = networkApi.getReadyReservations(clientId)
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

data class JoinUiState(
    val items: List<ReservationReadyResponse> = emptyList()
)