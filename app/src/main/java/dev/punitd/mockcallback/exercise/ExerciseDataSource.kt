package dev.punitd.mockcallback.exercise

import kotlinx.coroutines.delay
import java.time.ZonedDateTime

interface ExerciseDataSource {
    suspend fun getExerciseSession(daysOffset: Long): ExerciseSessionRecord
}

class ExerciseDataSourceImpl : ExerciseDataSource {
    override suspend fun getExerciseSession(daysOffset: Long): ExerciseSessionRecord {
        delay(2_00) // Added delay to simulate actual work
        return generateDummyExerciseSessionRecord()
    }

}

private fun generateDummyExerciseSessionRecord(): ExerciseSessionRecord {
    return ExerciseSessionRecord(
        title = "Morning Exercise",
        notes = "What a fun ride",
        startTime = ZonedDateTime.now().minusHours(2).toInstant(),
        endTime = ZonedDateTime.now().minusHours(1).toInstant(),
        exerciseType = ExerciseType.CYCLING,
        exerciseRoute = ExerciseRoute(
            route = listOf(
                ExerciseRoute.Location(
                    time = ZonedDateTime.now().minusHours(2).plusMinutes(2).toInstant(),
                    latitude = 4.5,
                    longitude = 6.7
                ),
                ExerciseRoute.Location(
                    time = ZonedDateTime.now().minusHours(2).plusMinutes(4).toInstant(),
                    latitude = 4.5,
                    longitude = 6.7
                )
            )
        )
    )
}