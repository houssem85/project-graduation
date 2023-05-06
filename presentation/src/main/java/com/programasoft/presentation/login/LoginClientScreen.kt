package com.programasoft.presentation.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.programasoft.presentation.R


@Composable
fun LoginClientRoute(
    viewModel: LoginClientViewModel = hiltViewModel(),
    onSignUpClicked: () -> Unit = {},
    onUserLoggedIn: () -> Unit = {},
) {
    val uiState: LoginClientUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState.isLogged) {
        if (uiState.isLogged) {
            onUserLoggedIn()
        }
    }

    LoginClientScreen(
        uiState,
        viewModel::enterEmail,
        viewModel::enterPassword,
        viewModel::login,
        onSignUpClicked = onSignUpClicked
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginClientScreen(
    uiState: LoginClientUiState,
    onEmailChanged: (TextFieldValue) -> Unit,
    onPasswordChanged: (TextFieldValue) -> Unit,
    onLogInClicked: () -> Unit = {},
    onSignUpClicked: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)

    ) {
        Text(
            text = "sign in",
            fontSize = 32.sp,
            color = Color(0xFF3F5AA6),
        )
        Spacer(modifier = Modifier.size(40.dp))
        Image(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null
        )
        val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF3F5AA6),
            cursorColor = Color(0xFF3F5AA6)
        )
        Spacer(modifier = Modifier.size(20.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.email,
            onValueChange = onEmailChanged,
            label = { Text("email") },
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            colors = textFieldColors
        )
        Spacer(modifier = Modifier.size(20.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.password,
            onValueChange = onPasswordChanged,
            label = { Text("password") },
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            colors = textFieldColors,
            visualTransformation = PasswordVisualTransformation(),
        )

        Spacer(modifier = Modifier.size(20.dp))
        val customButtonColors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF3F5AA6),
        )
        Button(
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = customButtonColors,
            onClick = onLogInClicked
        ) {
            Text("Log in")
        }
        Spacer(modifier = Modifier.size(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "If you don't have an account",
                color = Color.Black,
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                modifier = Modifier.clickable {
                    onSignUpClicked.invoke()
                },
                text = "Sign up",
                color = Color(0xFF3F5AA6),
                fontSize = 16.sp,
            )
        }
        Spacer(modifier = Modifier.size(20.dp))
        if (uiState.errorMessage.isNotEmpty()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = uiState.errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun LoginClientScreenPreview() {
    LoginClientScreen(
        LoginClientUiState(),
        {}, {}
    )
}