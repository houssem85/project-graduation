package com.programasoft.presentation.newavailabilitygroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programasoft.data.network.NetworkApi
import com.programasoft.data.network.model.AvailabilityRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class NewAvailabilityGroupViewModel @Inject constructor(
    private val networkApi: NetworkApi
) : ViewModel() {

    private val _uiState: MutableStateFlow<NewAvailabilityGroupUiState> = MutableStateFlow(
        NewAvailabilityGroupUiState()
    )
    val uiState = _uiState.asStateFlow()

    fun onSelectStartDate(date: LocalDate) {
        _uiState.update {
            it.copy(startDate = date)
        }
    }

    fun onSelectEndDate(date: LocalDate) {
        _uiState.update {
            it.copy(endDate = date)
        }
    }

    fun addAvailability(key: String, timeInterval: TimeInterval) {
        _uiState.update {
            it.copy(
                days = addToMap(key, timeInterval, it.days)
            )
        }
    }

    fun delete(key: String, timeInterval: TimeInterval) {
        _uiState.update {
            it.copy(
                days = deleteFromMap(key, timeInterval, it.days)
            )
        }
    }

    fun deleteFromMap(
        key: String,
        interval: TimeInterval,
        map: Map<String, List<TimeInterval>>
    ): Map<String, List<TimeInterval>> {
        val existingList = map[key] ?: emptyList()

        val updatedList = existingList - interval

        return map + (key to updatedList)
    }

    fun createAvailabilities(psychologistId: Long) {
        viewModelScope.launch {
            val isDatesAvailable =
                _uiState.value.startDate != null && _uiState.value.endDate != null
            if (isDatesAvailable) {
                val isStartDateBeforeEndDate =
                    _uiState.value.startDate!!.isBefore(_uiState.value.endDate)
                if (isStartDateBeforeEndDate) {
                    val request = mapAvailabilityGroupUiStateToRequest(
                        _uiState.value,
                        psychologistId
                    )
                    try {
                        val res = networkApi.createAvailabilities(request)
                        if (res.code() == 200) {
                            _uiState.update {
                                it.copy(
                                    isCompleted = true
                                )
                            }
                        } else if (res.code() == 409) {
                            _uiState.update {
                                it.copy(
                                    errorMessage = "Overlapping availabilities detected. Please choose different timings"
                                )
                            }
                        } else {
                            _uiState.update {
                                it.copy(
                                    errorMessage = "error server : ${res.code()}"
                                )
                            }
                        }
                    } catch (ex: Exception) {
                        _uiState.update {
                            it.copy(
                                errorMessage = ex.message
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            errorMessage = "La date de début est ultérieure à la date de fin. Veuillez saisir des dates valides"
                        )
                    }
                }
            } else {
                _uiState.update {
                    it.copy(
                        errorMessage = "plz enter the start date and end date"
                    )
                }
            }
        }
    }
}

fun addToMap(
    key: String,
    interval: TimeInterval,
    map: Map<String, List<TimeInterval>>
): Map<String, List<TimeInterval>> {
    val existingList = map[key] ?: emptyList()
    val updatedList = existingList + interval
    return map + (key to updatedList)
}

data class NewAvailabilityGroupUiState(
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val days: Map<String, List<TimeInterval>> = mapOf(
        "sunday" to emptyList(),
        "monday" to emptyList(),
        "tuesday" to emptyList(),
        "wednesday" to emptyList(),
        "thursday" to emptyList(),
        "friday" to emptyList(),
        "saturday" to emptyList()
    ),
    val errorMessage: String? = null,
    val isCompleted: Boolean = false
)

data class TimeInterval(
    val start: String,
    val end: String
)

fun mapAvailabilityGroupUiStateToRequest(
    uiState: NewAvailabilityGroupUiState,
    psychologistId: Long
): AvailabilityRequest {
    val startDate = uiState.startDate ?: throw IllegalArgumentException("StartDate is null")
    val endDate = uiState.endDate ?: throw IllegalArgumentException("EndDate is null")
    val days = uiState.days.mapValues { (_, timeIntervals) ->
        timeIntervals.map { interval ->
            com.programasoft.data.network.model.TimeInterval(interval.start, interval.end)
        }
    }
    val startDateFormatted = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val endDateFormatted = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    return AvailabilityRequest(startDateFormatted, endDateFormatted, days, psychologistId)
}