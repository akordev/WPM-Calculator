package com.example.typinganalyze.typing.usecase

import android.util.Log
import com.example.typinganalyze.typing.data.KeyEventAnalyticsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map


interface WPMCalculator {
    suspend operator fun invoke(): Flow<Int>
}

class WPMCalculatorIml(
    private val keyEventAnalyticsDao: KeyEventAnalyticsDao,
    private val dispatcher: CoroutineDispatcher
) : WPMCalculator {

    private var typingStarted: Long? = null

    /**
     * TODO: must be unit tested
     * TODO: collect only last updates. Now we query all db and iterate everything on each db update, basically slowest solution
     * Idea is to subscribe to only one new changes, keyEventAnalyticsDao.getUpdates() : FLow<KeyEventAnalytics> instead if FLow<List<KeyEventAnalytics>>
     * The code should look something like:
     *
     * keyEventAnalyticsDao
     *  .getUpdates()
     *  .onTimeout{
     *      // updated pauses list
     *  }
     *  .map {
     *      // apply last item changes and transform to WPM value
     *  }
     */
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

                Log.d("WPM", "$timeInMinutes, ${events.size},  $wpm")

                wpm.toInt()
            }.flowOn(dispatcher)
    }
}