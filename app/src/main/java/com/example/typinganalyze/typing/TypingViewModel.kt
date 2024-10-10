package com.example.typinganalyze.typing

import android.util.Log
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType.Companion.KeyDown
import androidx.compose.ui.input.key.KeyEventType.Companion.KeyUp
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.typinganalyze.typing.data.KeyEventAnalytics
import com.example.typinganalyze.typing.usecase.FindUnreleasedEventUseCase
import com.example.typinganalyze.typing.usecase.GetTextToTypeUseCase
import com.example.typinganalyze.typing.usecase.SaveKeyEventUseCase
import com.example.typinganalyze.typing.usecase.WPMCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TypingViewModel(
    username: String,
    getTextToTypeUseCase: GetTextToTypeUseCase,
    private val saveKeyEventUseCase: SaveKeyEventUseCase,
    private val findUnreleasedEventUseCase: FindUnreleasedEventUseCase,
    private val wpmCalculator: WPMCalculator
) : ViewModel() {

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(
        ViewState(
            paragraph = getTextToTypeUseCase(),
            wpm = 0,
            typedText = "",
            username = username
        )
    )
    val viewState: StateFlow<ViewState>
        get() = _viewState.asStateFlow()


    init {
        viewModelScope.launch {
            wpmCalculator.invoke().collect {
                _viewState.value = _viewState.value.copy(
                    wpm = it
                )
            }
        }
    }

    fun handleEvents(event: Event) = when (event) {
        is Event.KeyClicked -> onKeyEvent(event.keyEvent)
        is Event.TextChanged -> updateTypedText(event.newText)
    }


    private fun onKeyEvent(keyEvent: KeyEvent) {
        Log.d(TAG, "Key event received: $keyEvent")

        val keyPressTime = System.currentTimeMillis()
        val keyCode = keyEvent.key.keyCode

        viewModelScope.launch {
            if (keyEvent.type == KeyDown) {
                // Save new key press event
                val keyEventAnalytics = KeyEventAnalytics(
                    keyCode = keyCode,
                    keyPressTime = keyPressTime,
                    keyReleaseTime = null,
                    username = _viewState.value.username
                )
                saveKeyEventUseCase(keyEventAnalytics)
            } else if (keyEvent.type == KeyUp) {
                // Handle key release, find the corresponding press event and update it
                val lastPressedEvent = findUnreleasedEventUseCase(keyCode)

                if (lastPressedEvent != null) {
                    val updatedEvent = lastPressedEvent.copy(keyReleaseTime = keyPressTime)
                    saveKeyEventUseCase(updatedEvent) // This should update instead of insert
                }
            }
        }
    }

    private fun updateTypedText(text: String) {
        _viewState.value = _viewState.value.copy(
            typedText = text
        )
    }

    data class ViewState(
        val username: String,
        val paragraph: String,
        val wpm: Int,
        val typedText: String
    )

    sealed interface Event {
        data class TextChanged(val newText: String) : Event
        data class KeyClicked(val keyEvent: KeyEvent) : Event
    }

    companion object {
        private val TAG = TypingViewModel::class.java.name
    }
}