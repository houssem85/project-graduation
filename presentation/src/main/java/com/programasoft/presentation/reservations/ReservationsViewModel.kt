package com.programasoft.presentation.reservations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programasoft.data.network.NetworkApi
import com.programasoft.data.network.model.ReservationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationsViewModel @Inject constructor(
    private val networkApi: NetworkApi
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReservationsUiState())
    val uiState = _uiState.asStateFlow()

    fun getData(accountId: Long, clientId: Long, isPaid: Boolean) {
        viewModelScope.launch {
            try {
                val res = networkApi.getReservations(clientId, isPaid)
                val resBalance = networkApi.getBalance(accountId)
                if (res.code() == 200) {
                    _uiState.update {
                        it.copy(
                            reservations = res.body()!!,
                            balance = resBalance.body()!!.get("balance")!!
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            errorMessage = res.code().toString()
                        )
                    }
                }
            } catch (ex: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = ex.message!!
                    )
                }
            }
        }
    }

    fun onPaymentClick(reservationId: Long,accountId: Long, clientId: Long, isPaid: Boolean) {
        viewModelScope.launch {
            try {
                val res = networkApi.paymentReservation(reservationId)
                if (res.code() == 200) {
                    _uiState.update {
                        it.copy(
                            errorMessage = "Payment successful! Reservation confirmed."
                        )
                    }
                    getData(accountId,clientId,isPaid)
                } else if (res.code() == 401) {
                    _uiState.update {
                        it.copy(
                            errorMessage = "Your account balance is insufficient"
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            errorMessage = res.code().toString()
                        )
                    }
                }
            } catch (ex: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = ex.message!!
                    )
                }
            }
        }
    }

    fun errorMessageShown() {
        _uiState.update {
            it.copy(
                errorMessage = ""
            )
        }
    }
}

data class ReservationsUiState(
    val reservations: List<ReservationResponse> = emptyList(),
    val balance: Double = 0.0,
    val errorMessage: String = ""
)