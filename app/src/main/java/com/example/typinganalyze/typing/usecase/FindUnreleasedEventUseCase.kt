package com.example.typinganalyze.typing.usecase

import com.example.typinganalyze.typing.data.KeyEventAnalytics
import com.example.typinganalyze.typing.data.KeyEventAnalyticsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FindUnreleasedEventUseCase(
    private val localDataSource: KeyEventAnalyticsDao,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(keyCode: Long) = withContext(dispatcher) {
        localDataSource.findLastPressedEvent(keyCode)
    }
}