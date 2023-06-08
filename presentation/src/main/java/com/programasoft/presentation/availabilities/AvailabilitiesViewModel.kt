package com.programasoft.presentation.availabilities

import androidx.lifecycle.ViewModel
import com.programasoft.data.network.NetworkApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AvailabilitiesViewModel @Inject constructor(
    private val networkApi: NetworkApi
) : ViewModel() {
}