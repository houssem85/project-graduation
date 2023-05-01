package com.programasoft.presentation.psychologistprofile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programasoft.data.network.NetworkApi
import com.programasoft.data.network.model.Psychologist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PsychologistProfileViewModel @Inject constructor(
    private val networkApi: NetworkApi,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PsychologistProfileUiState())
    val uiState: StateFlow<PsychologistProfileUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                val res = networkApi.getPsychologist(savedStateHandle["psychologistId"]!!)
                val code = res.code()
                if (code == 200) {
                    _uiState.update {
                        PsychologistProfileUiState(
                            isLoading = false,
                            psychologist = res.body()!!
                        )
                    }
                } else {
                    _uiState.update {
                        PsychologistProfileUiState(
                            isLoading = false,
                            errorMessage = code.toString()
                        )
                    }
                }
            } catch (ex: Exception) {
                _uiState.update {
                    PsychologistProfileUiState(
                        isLoading = false,
                        errorMessage = ex.message!!
                    )
                }
            }
        }
    }
}

data class PsychologistProfileUiState(
    val isLoading: Boolean = true,
    val psychologist: Psychologist? = null,
    val errorMessage: String = "",
)