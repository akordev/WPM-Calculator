package com.example.typinganalyze.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onContinueClick: (String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    Scaffold { innerPaddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings)
                .padding(16.dp)
        ) {
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Enter your username") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onContinueClick.invoke(username) },
                enabled = username.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Typing")
            }
        }
    }
}






