package com.example.typinganalyze.typing.usecase

import com.example.typinganalyze.typing.data.KeyEventAnalyticsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map


// We probably want to fake it for unit tests
// We also might want to have different implementations, different rules
interface WPMCalculator {
    suspend operator fun invoke(): Flow<Int>
}

class WPMCalculatorIml(
    private val keyEventAnalyticsDao: KeyEventAnalyticsDao,
    private val dispatcher: CoroutineDispatcher
) : WPMCalculator {

    private var typingStarted: Long? = null

    override suspend fun invoke(): Flow<Int> {
        keyEventAnalyticsDao.clearAllKeyEvents()

        return keyEventAnalyticsDao
            .getAllEvents()
            .map { events ->

                if (events.isEmpty()) {
                    return@map 0
                }

                if (typingStarted == null) {
                    typingStarted = events.first().keyPressTime
                }

                val currentTime = System.currentTimeMillis()

                var pauses = 0L

                for (i in 1 until events.size) {
                    val currentEvent = events[i]
                    val previousEvent = events[i - 1]
                    // Calculate the pause time
                    val pauseTime = currentEvent.keyPressTime - previousEvent.keyPressTime

                    // Only add pause more than 5 sec times, 5 sec is magic number in this case.
                    // Should be discussed and probably dynamically fetched from fb config or similar
                    if (pauseTime > 5000) {
                        pauses += pauseTime - 5000
                    }
                }

                val totalTime = currentTime - (typingStarted ?: 0) - pauses

                val timeInMinutes = totalTime / (1000.0 * 60.0) // converting ms to minutes
                val wpm = if (timeInMinutes > 0) {
                    (events.size / 5.0) / timeInMinutes
                } else {
                    0.0
                }

                wpm.toInt()
            }.flowOn(dispatcher)
    }
}