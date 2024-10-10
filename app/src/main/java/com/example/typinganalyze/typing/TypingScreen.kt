package com.example.typinganalyze.typing

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreInterceptKeyBeforeSoftKeyboard
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TypingScreen(
    userName: String,
    viewModel: TypingViewModel = koinViewModel<TypingViewModel> { parametersOf(userName) }
) {

    val viewState = viewModel.viewState.collectAsStateWithLifecycle()

    val viewStateValue = viewState.value

    TypingScreenContent(
        viewState = viewStateValue,
        onValueChanged = { newText ->
            viewModel.handleEvents(TypingViewModel.Event.TextChanged(newText))
        },
        onPreInterceptKeyBeforeSoftKeyboard = { keyEvent ->
            viewModel.handleEvents(TypingViewModel.Event.KeyClicked(keyEvent))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TypingScreenContent(
    viewState: TypingViewModel.ViewState,
    onValueChanged: (String) -> Unit,
    onPreInterceptKeyBeforeSoftKeyboard: (KeyEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(viewState.username) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Display the reference paragraph
            Text(
                text = viewState.paragraph,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            // TextField for the user to type into
            TextField(
                value = viewState.typedText,
                onValueChange = { newText ->
                    onValueChanged.invoke(newText)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .onPreInterceptKeyBeforeSoftKeyboard { keyEvent ->
                        if (keyEvent.key == Key.Backspace) {
                            return@onPreInterceptKeyBeforeSoftKeyboard true
                        }

                        onPreInterceptKeyBeforeSoftKeyboard.invoke(keyEvent)

                        return@onPreInterceptKeyBeforeSoftKeyboard false
                    }
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Display the real-time WPM
            Text(
                text = "WPM: ${viewState.wpm}",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true
)
@Composable
fun TypingScreenPreview() {
    MaterialTheme {
        TypingScreenContent(
            viewState = TypingViewModel.ViewState(
                "dasdf", "fasdfgsfgsdfg", 10, "ajsdifnksf",
            ), {}, {}
        )
    }
}