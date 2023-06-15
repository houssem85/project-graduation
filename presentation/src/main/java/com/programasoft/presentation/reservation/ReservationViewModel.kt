package com.programasoft.presentation.reservation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programasoft.data.network.NetworkApi
import com.programasoft.data.network.model.CreateReservationRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(
    private val network: NetworkApi,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState: MutableStateFlow<ReservationUiState> = MutableStateFlow(
        ReservationUiState()
    )
    val uiState = _uiState.asStateFlow()

    private val psychologistId: Long = savedStateHandle["psychologistId"]!!

    init {
        viewModelScope.launch {
            try {
                val response = network.getAvailableDays(
                    psychologistId = psychologistId
                )
                if (response.code() == 200) {
                    _uiState.update {
                        ReservationUiState(isLoading = false,
                            errorMessage = "",
                            availableDates = response.body()!!.map {
                                it.toTimeStamp()
                            })
                    }
                } else {
                    _uiState.update {
                        ReservationUiState(
                            isLoading = false, errorMessage = response.code().toString()
                        )
                    }
                }
            } catch (ex: Exception) {
                _uiState.update {
                    ReservationUiState(
                        isLoading = false, errorMessage = ex.message!!
                    )
                }
            }
        }
    }

    fun selectDate(date: Long) {
        viewModelScope.launch {
            val date = formatDate(date)
            try {
                val response = network.getAvailableUnits(
                    psychologistId, date
                )
                if (response.code() == 200) {
                    _uiState.update {
                        it.copy(availabilityUnits = response.body()!!.map {
                            AvailabilityUnitUiState(id = it.id,
                                startDate = convertLocalDateTime(it.start),
                                endDate = convertLocalDateTime(it.end),
                                onClick = {
                                    onClick(it.id)
                                })
                        })
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            errorMessage = response.code().toString()
                        )
                    }
                }
            } catch (ex: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = it.errorMessage
                    )
                }
            }
        }
    }

    private fun onClick(id: Long) {
        _uiState.update {
            val newList = arrayListOf<AvailabilityUnitUiState>()
            newList.addAll(it.availabilityUnits.map {
                if (it.id == id) {
                    it.copy(
                        isSelected = !it.isSelected
                    )
                } else {
                    it.copy()
                }
            })
            it.copy(
                availabilityUnits = newList
            )
        }
    }

    fun validate(clientId : Long) {
        val test = areSelectedAvailabilityUnitsAdjacent(
            _uiState.value.availabilityUnits
        )
        if (test) {
            viewModelScope.launch {
                try {
                    _uiState.update {
                        it.copy(
                            isLoading = true,
                            errorMessage = ""
                        )
                    }
                    val res =
                        network.createReservation(reservationRequest = CreateReservationRequest(
                            clientId = clientId,
                            _uiState.value.availabilityUnits.filter {
                                it.isSelected
                            }.map {
                                it.id
                            }.toSet()
                        )
                        )
                    if (res.code() == 200) {
                        _uiState.update {
                            it.copy(
                                isLoading = false, isReservationSuccess = true
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false, errorMessage = res.code().toString()
                            )
                        }
                    }
                } catch (ex: Exception) {
                    _uiState.update {
                        it.copy(
                            isLoading = false, errorMessage = ex.message!!
                        )
                    }
                }
            }
        } else {
            _uiState.update {
                it.copy(
                    isLoading = false, errorMessage = "Plz select adjacents availabilities"
                )
            }
        }
    }

    fun areSelectedAvailabilityUnitsAdjacent(units: List<AvailabilityUnitUiState>): Boolean {
        val selectedUnits = units.filter { it.isSelected }.sortedBy { it.startDate }
        for (i in 0 until selectedUnits.size - 1) {
            val currentUnit = selectedUnits[i]
            val nextUnit = selectedUnits[i + 1]
            if (currentUnit.endDate != nextUnit.startDate) {
                return false
            }
        }
        return true
    }
}

private fun String.toTimeStamp(): Long {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = dateFormat.parse(this)
    return date.time
}

private fun dateTimeRangeToString(
    startDateTime: LocalDateTime,
    endDateTime: LocalDateTime,
): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val startTimeString = startDateTime.format(formatter)
    val endTimeString = endDateTime.format(formatter)
    return "$startTimeString -> $endTimeString"
}


data class ReservationUiState(
    val isLoading: Boolean = true,
    val availableDates: List<Long> = listOf(),
    val availabilityUnits: List<AvailabilityUnitUiState> = listOf(),
    val errorMessage: String = "",
    val isReservationSuccess: Boolean = false,
)

data class AvailabilityUnitUiState(
    val id: Long,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    var isSelected: Boolean = false,
    val onClick: () -> Unit,
)

fun AvailabilityUnitUiState.getTimeRange(): String {
    val start = startDate.format(DateTimeFormatter.ofPattern("HH:mm"))
    val end = endDate.format(DateTimeFormatter.ofPattern("HH:mm"))
    return "$start -> $end"
}

fun formatDate(timestamp: Long): String {
    val instant = Instant.ofEpochMilli(timestamp)
    val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return localDate.format(formatter)
}

fun convertLocalDateTime(chaine: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    return LocalDateTime.parse(chaine, formatter)
}