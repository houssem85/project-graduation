@file:OptIn(ExperimentalMaterial3Api::class)

package com.programasoft.presentation.psychologists

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.programasoft.presentation.R

@Composable
fun PsychologistsRoute(
    viewModel: PsychologistsViewModel = hiltViewModel(),
    onClick: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PsychologistsScreen(
        onTextChanged = viewModel::search,
        psychologistsUiState = uiState,
        onClick = onClick
    )
}

@Composable
fun PsychologistsScreen(
    psychologistsUiState: PsychologistsUiState,
    onTextChanged: (TextFieldValue) -> Unit,
    onClick: (Int) -> Unit,
) {
    val avenir = FontFamily(
        Font(R.font.avenir, FontWeight.Normal),
        Font(R.font.avenir, FontWeight.Light),
        Font(R.font.avenir, FontWeight.SemiBold),
        Font(R.font.avenir, FontWeight.ExtraBold)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 20.dp, end = 20.dp)
            .background(Color.White)
    ) {
        TextField(
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                textColor = Color.Black,
                cursorColor = Color(0xFF3F5AA6),
                focusedIndicatorColor = Color(0xFF3F5AA6)
            ),
            modifier = Modifier.fillMaxWidth(),
            value = psychologistsUiState.searchText,
            onValueChange = onTextChanged,
            singleLine = true,
            trailingIcon = {
                Icon(imageVector = Icons.Filled.Search, contentDescription = null)
            }
        )
        Spacer(modifier = Modifier.size(20.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(psychologistsUiState.filteredData) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .clickable {
                            onClick(it.id.toInt())
                        }
                ) {
                    Spacer(modifier = Modifier.size(10.dp))
                    Row(
                    ) {
                        if (it.image.toBitmap() != null) {
                            Image(
                                modifier = Modifier
                                    .size(60.dp),
                                bitmap = it.image.toBitmap()!!.asImageBitmap(),
                                contentDescription = null
                            )
                        } else {
                            Image(
                                modifier = Modifier
                                    .size(60.dp),
                                imageVector = Icons.Rounded.AccountCircle,
                                contentDescription = null
                            )
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = it.account.fullName,
                                fontFamily = avenir,
                                color = Color(
                                    0xFF3F5AA6
                                )
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            Text(
                                text = it.presentation,
                                fontFamily = avenir,
                                color = Color(
                                    0xFF828899
                                ),
                                maxLines = 5,
                                fontSize = 13.sp,
                                lineHeight = 16.sp,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "${it.hourlyRate} dt/h",
                                fontFamily = avenir,
                                color = Color(
                                    0xFF3F5AA6
                                ),
                                fontSize = 14.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(
                                Color(0xFF3F5AA6)
                            )
                    )
                }
            }
        }
    }
}

fun String.toBitmap(): Bitmap? {
    if (this.isNotEmpty()) {
        val decodedString = Base64.decode(this, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    } else {
        return null
    }
}