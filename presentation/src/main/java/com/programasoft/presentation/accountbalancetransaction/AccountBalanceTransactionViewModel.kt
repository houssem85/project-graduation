package com.programasoft.presentation.accountbalancetransaction

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programasoft.data.network.NetworkApi
import com.programasoft.data.network.model.TransactionResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountBalanceTransactionViewModel @Inject constructor(
    private val networkApi: NetworkApi
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountBalanceTransactionUiState())
    val uiState: StateFlow<AccountBalanceTransactionUiState> = _uiState

    private val message = mutableStateOf("")

    fun loadData(accountId: Long) {
        viewModelScope.launch {
            _uiState.update {
                AccountBalanceTransactionUiState()
            }
            try {
                val balanceRes = networkApi.getBalance(accountId)
                val listRes = networkApi.getPaymentHistory(accountId)
                if (balanceRes.code() == 200 && listRes.code() == 200) {
                    val map = balanceRes.body()!!
                    _uiState.update {
                        AccountBalanceTransactionUiState(
                            loading = false,
                            balance = map["balance"]!!,
                            creditBalance = map["creditBalance"]!!,
                            debitBalance = map["debitBalance"]!!,
                            items = listRes.body()!!
                        )
                    }
                } else {
                    _uiState.update {
                        AccountBalanceTransactionUiState(
                            loading = false
                        )
                    }
                    message.value = balanceRes.code().toString()
                }
            } catch (ex: Exception) {
                _uiState.update {
                    AccountBalanceTransactionUiState(
                        loading = false
                    )
                }
                message.value = ex.message!!
            }
        }
    }

    fun messageIsShown() {
        message.value = ""
    }
}


data class AccountBalanceTransactionUiState(
    val loading: Boolean = true,
    val balance: Double = 0.0,
    val creditBalance: Double = 0.0,
    val debitBalance: Double = 0.0,
    val items: List<TransactionResponse> = emptyList()
)