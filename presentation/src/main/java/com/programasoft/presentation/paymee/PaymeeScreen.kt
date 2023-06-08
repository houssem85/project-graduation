package com.programasoft.presentation.paymee

import android.content.Context
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@Composable
fun PaymeeRoute(
    viewModel: PaymeeViewModel = hiltViewModel(),
    onFinish: () -> Unit,
) {

    val uiState: PaymeeUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val sharedPref = context.getSharedPreferences("project-graduation", Context.MODE_PRIVATE)
        val accountId = sharedPref.getLong("account_id", 0)
        viewModel.doPayment(accountId)
    }

    LaunchedEffect(uiState) {
        if (uiState.errorMessage != null) {
            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_LONG).show()
            onFinish()
        }
    }

    LaunchedEffect(uiState) {
        if (uiState.isPaymentStoreDone) {
            onFinish()
        }
    }

    PaymeeScreen(uiState, onFinish) {
        viewModel.paymentStore()
    }
}

@Composable
fun PaymeeScreen(
    uiState: PaymeeUiState,
    onFinish: () -> Unit,
    onStore: () -> Unit,
) {

    val state = rememberWebViewState(uiState.url)

    val client = remember {
        CustomWebViewClient {
            it?.let {
                if (it.contains("https://www.paymee.tn")) {
                    onStore()
                }
                if (it.contains("https://www.cancel.tn")) {
                    onFinish()
                }
            }
        }
    }
    WebView(
        client = client,
        state = state
    )
}

class CustomWebViewClient(val onChangeUrl: (String?) -> Unit) : AccompanistWebViewClient() {
    override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
        super.doUpdateVisitedHistory(view, url, isReload)
        onChangeUrl(url)
    }
}