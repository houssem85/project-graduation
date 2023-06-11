package com.programasoft.presentation.availabilities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programasoft.data.network.NetworkApi
import com.programasoft.data.network.model.AvailabilityGroup
import com.programasoft.presentation.accountbalancetransaction.AccountBalanceTransactionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvailabilitiesViewModel @Inject constructor(
    private val networkApi: NetworkApi
) : ViewModel() {
    private val _uiState = MutableStateFlow(AvailabilitiesUiState())
    val uiState: StateFlow<AvailabilitiesUiState> = _uiState

    fun getData(psychologistId: Long) {
        viewModelScope.launch {
            try {
                val res = networkApi.getAvailabilityGroups(psychologistId)
                if (res.code() == 200) {
                    _uiState.update {
                        it.copy(
                            data = res.body()!!
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

    fun messageIsShown() {
        _uiState.update {
            it.copy(
                errorMessage = null
            )
        }
    }
}

data class AvailabilitiesUiState(
    val data: List<AvailabilityGroup> = emptyList(),
    val errorMessage: String? = null
)