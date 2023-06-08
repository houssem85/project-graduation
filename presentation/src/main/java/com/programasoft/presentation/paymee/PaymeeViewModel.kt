package com.programasoft.presentation.paymee

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.programasoft.data.network.NetworkApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymeeViewModel @Inject constructor(
    private val networkApi: NetworkApi,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val amount: Float = checkNotNull(savedStateHandle["amount"])

    private val _uiState = MutableStateFlow(PaymeeUiState())
    val uiState: StateFlow<PaymeeUiState> = _uiState

    private var token: String = ""
    private var accountId: Long = 0L

    fun doPayment(accountId: Long) {
        this.accountId = accountId
        viewModelScope.launch {
            try {
                val json = JsonObject()
                json.addProperty("amount", amount)
                json.addProperty("note", accountId.toString())
                json.addProperty("first_name", "John")
                json.addProperty("last_name", "Doe")
                json.addProperty("email", "test@paymee.tn")
                json.addProperty("phone", "+21611222333")
                json.addProperty("return_url", "https://www.paymee.tn")
                json.addProperty("cancel_url", "https://www.cancel.tn")
                json.addProperty("webhook_url", "https://localhost:8080/api/v1/transactions")
                val res = networkApi.doPayment(
                    "https://sandbox.paymee.tn/api/v2/payments/create",
                    json,
                    "Token c77f32c4ea3c4427dffa7673a21f4b575439e074"
                )
                if (res.code() == 200) {
                    _uiState.update {
                        PaymeeUiState(
                            errorMessage = null,
                            loading = false,
                            url = res.body()!!.data.paymentUrl
                        )
                    }
                    token = res.body()!!.data.token
                } else {
                    _uiState.update {
                        PaymeeUiState(
                            errorMessage = res.code().toString(),
                            loading = false,
                            url = ""
                        )
                    }
                }
            } catch (ex: Exception) {
                _uiState.update {
                    PaymeeUiState(
                        errorMessage = ex.message!!,
                        loading = false,
                        url = ""
                    )
                }
            }
        }
    }

    fun paymentStore() {
        viewModelScope.launch {
            try {
                val res = networkApi.getPaymentStatus(
                    "https://sandbox.paymee.tn/api/v1/payments/$token/check",
                    "Token c77f32c4ea3c4427dffa7673a21f4b575439e074"
                )
                if (res.code() == 200) {
                    if (res.body()!!.data.paymentStatus) {
                        val json = JsonObject()
                        json.addProperty("account_id", accountId)
                        json.addProperty("amount", res.body()!!.data.amount)
                        json.addProperty("transaction_id", res.body()!!.data.transactionId)
                        networkApi.paymentStore(json)
                        _uiState.update {
                            it.copy(
                               isPaymentStoreDone = true
                            )
                        }
                    }
                }
            } catch (ex: Exception) {

            }
        }
    }

}

data class PaymeeUiState(
    val errorMessage: String? = null,
    val loading: Boolean = true,
    val url: String = "",
    val isPaymentStoreDone: Boolean = false
)