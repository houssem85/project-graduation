package com.programasoft.presentation.register

import android.util.Patterns
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.programasoft.data.network.NetworkApi
import com.programasoft.data.network.model.Account
import com.programasoft.data.network.model.Client
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject


@HiltViewModel
class RegisterClientViewModel @Inject constructor(
    private val networkApi: NetworkApi,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterClientUiState())
    val uiState: StateFlow<RegisterClientUiState> = _uiState


    fun enterEmail(email: TextFieldValue) {
        _uiState.update {
            it.copy(
                email = email,
                errorMessage = ""
            )
        }
    }

    fun enterPassword(password: TextFieldValue) {
        _uiState.update {
            it.copy(
                password = password,
                errorMessage = ""
            )
        }
    }

    fun enterFullName(fullName: TextFieldValue) {
        _uiState.update {
            it.copy(
                fullName = fullName,
                errorMessage = ""
            )
        }
    }

    fun signUp() {
        val email = _uiState.value.email.text
        val password = _uiState.value.password.text
        val fullName = _uiState.value.fullName.text
        val isValid = isValidEmail(email) && password.isNotEmpty() && fullName.isNotEmpty()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            // Get new FCM registration token
            val token = task.result
            if (isValid) {
                viewModelScope.launch {
                    try {
                        val account = Account(
                            id = 0,
                            email = email,
                            password = password,
                            fullName = fullName,
                            connectedDeviceId = token
                        )
                        val client = Client(
                            account = account
                        )
                        val response = networkApi.register(client)
                        if (response.code() == 200) {
                            _uiState.update {
                                it.copy(
                                    isSuccess = true,
                                    errorMessage = ""
                                )
                            }
                        } else if (response.code() == 409) {
                            _uiState.update {
                                it.copy(
                                    errorMessage = "this email already used"
                                )
                            }
                        } else {
                            _uiState.update {
                                it.copy(
                                    errorMessage = "error ${response.code()}"
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
            } else {
                _uiState.update {
                    it.copy(
                        errorMessage = "Your form contains errors. Please correct them"
                    )
                }
            }
        })
    }
}

private fun isValidEmail(email: String): Boolean {
    val pattern: Pattern = Patterns.EMAIL_ADDRESS
    return pattern.matcher(email).matches()
}

data class RegisterClientUiState(
    val email: TextFieldValue = TextFieldValue(),
    val password: TextFieldValue = TextFieldValue(),
    val fullName: TextFieldValue = TextFieldValue(),
    val isSuccess: Boolean = false,
    val errorMessage: String = "",
)

