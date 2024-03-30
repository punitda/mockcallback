package dev.punitd.mockcallback.exercise

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ExerciseRepository(
    private val exerciseDataSource: ExerciseDataSource,
    private val legacyDbManager: LegacyDbManager,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun syncExercise() {
        val daysOffset = calculateOffset()
        val sessionRecord = exerciseDataSource.getExerciseSession(daysOffset)
        saveRecord(sessionRecord)
        saveRecordToLegacyDb(sessionRecord)
    }

    private suspend fun calculateOffset(): Long {
        delay(2_00) // Added delay to simulate actual work
        return 2
    }

    private suspend fun saveRecord(record: ExerciseSessionRecord) =
        withContext(ioDispatcher) {
            delay(1_000) // Added delay to simulate actual work
        }


    private suspend fun saveRecordToLegacyDb(record: ExerciseSessionRecord) = suspendCoroutine {
        legacyDbManager.syncExercise(record) {
            it.resume(Unit)
        }
    }
}