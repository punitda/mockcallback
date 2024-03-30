package dev.punitd.mockcallback.exercise

import java.time.Instant

data class ExerciseSessionRecord(
    val title: String,
    val notes: String,
    val startTime: Instant,
    val endTime: Instant,
    val exerciseType: ExerciseType,
    val exerciseRoute: ExerciseRoute,
)


enum class ExerciseType {
    RUNNING,
    CYCLING,
}

data class ExerciseRoute(
    val route: List<Location>
) {
    data class Location(
        val time: Instant,
        val latitude: Double,
        val longitude: Double,
    )
}
