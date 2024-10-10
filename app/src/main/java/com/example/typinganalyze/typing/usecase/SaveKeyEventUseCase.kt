package com.example.typinganalyze.typing.usecase

import com.example.typinganalyze.typing.data.KeyEventAnalytics
import com.example.typinganalyze.typing.data.KeyEventAnalyticsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SaveKeyEventUseCase(
    private val localDataSource: KeyEventAnalyticsDao,
    private val dispatcher: CoroutineDispatcher
) {
        suspend operator fun invoke(keyEvent: KeyEventAnalytics) = withContext(dispatcher) {
            localDataSource.insertKeyEvent(keyEvent)
        }
}