package com.programasoft.presentation.psychologists

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programasoft.data.network.NetworkApi
import com.programasoft.data.network.model.Psychologist
import com.programasoft.presentation.login.LoginClientUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PsychologistsViewModel @Inject constructor(
    private val networkApi: NetworkApi,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PsychologistsUiState())
    val uiState: StateFlow<PsychologistsUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                val response = networkApi.getPsychologists()
                val code = response.code()
                if (response.code() == 200) {
                    _uiState.update {
                        PsychologistsUiState(
                            isLoading = false,
                            data = response.body()!!,
                            filteredData = response.body()!!
                        )
                    }
                } else {
                    _uiState.update {
                        PsychologistsUiState(
                            isLoading = false,
                            errorMessage = code.toString()
                        )
                    }
                }
            } catch (ex: Exception) {
                _uiState.update {
                    PsychologistsUiState(
                        isLoading = false,
                        errorMessage = ex.message!!
                    )
                }
            }
        }
    }

    fun search(newString: TextFieldValue) {
        _uiState.update {
            it.copy(
                isLoading = false,
                searchText = newString,
                filteredData = it.data.filter {
                    it.account.fullName.contains(newString.text)
                }
            )
        }
    }
}

data class PsychologistsUiState(
    val isLoading: Boolean = true,
    val searchText: TextFieldValue = TextFieldValue(""),
    val data: List<Psychologist> = listOf(),
    val filteredData: List<Psychologist> = listOf(),
    val errorMessage: String = "",
)