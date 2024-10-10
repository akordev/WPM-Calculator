package com.example.typinganalyze

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val userName = mutableStateOf("")
}