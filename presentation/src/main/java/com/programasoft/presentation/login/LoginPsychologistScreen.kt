package com.programasoft.presentation.login

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.programasoft.presentation.R
import com.programasoft.presentation.utils.roboto


@Composable
fun LoginPsychologistRoute(
    viewModel: LoginPsychologistViewModel = hiltViewModel(),
    onUserLoggedIn: () -> Unit = {},
) {
    val uiState: LoginPsychologistUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState.isLogged) {
        if (uiState.isLogged) {
            val sharedPref =
                context.getSharedPreferences("project-graduation", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putLong("account_id", uiState.psychologist!!.account.id)
                putLong("psychologist_id", uiState.psychologist!!.id)
                putString("role", "psychologist")
                apply()
            }
            onUserLoggedIn()
        }
    }

    LoginPsychologistScreen(
        uiState,
        viewModel::enterEmail,
        viewModel::enterPassword,
        viewModel::login,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPsychologistScreen(
    uiState: LoginPsychologistUiState,
    onEmailChanged: (TextFieldValue) -> Unit,
    onPasswordChanged: (TextFieldValue) -> Unit,
    onLogInClicked: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)

    ) {
        Text(
            text = "Sign in",
            fontFamily = roboto,
            fontWeight = FontWeight.Bold,
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
            cursorColor = Color(0xFF3F5AA6),
            focusedLabelColor = Color(0xFF3F5AA6)
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
            Text("Login", fontFamily = roboto, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.size(20.dp))
        if (uiState.errorMessage.isNotEmpty()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = uiState.errorMessage,
                fontFamily = roboto,
                fontWeight = FontWeight.Normal,
                color = Color.Red,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}