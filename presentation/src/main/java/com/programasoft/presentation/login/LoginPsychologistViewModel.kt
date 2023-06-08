package com.programasoft.presentation.login

import androidx.compose.ui.text.input.TextFieldValue
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
class LoginPsychologistViewModel @Inject constructor(
    private val networkApi: NetworkApi,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginPsychologistUiState())
    val uiState: StateFlow<LoginPsychologistUiState> = _uiState

    fun enterEmail(email: TextFieldValue) {
        _uiState.update {
            it.copy(email = email, errorMessage = "")
        }
    }

    fun enterPassword(password: TextFieldValue) {
        _uiState.update {
            it.copy(password = password, errorMessage = "")
        }
    }

    fun login() {
        val email = _uiState.value.email.text
        val password = _uiState.value.password.text
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = "")
            }
            try {
                val response = networkApi.login(email, password)
                if (response.code() == 200) {
                    if (response.body()!!.psychologist != null) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isLogged = true,
                                psychologist = response.body()!!.psychologist,
                                errorMessage = ""
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = "not allowed")
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "wrong email or password")
                    }
                }
            } catch (ex: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = ex.message!!)
                }
            }
        }
    }
}

data class LoginPsychologistUiState(
    val isLoading: Boolean = false,
    val email: TextFieldValue = TextFieldValue(),
    val password: TextFieldValue = TextFieldValue(),
    val isLogged: Boolean = false,
    val psychologist: Psychologist? = null,
    val errorMessage: String = "",
)