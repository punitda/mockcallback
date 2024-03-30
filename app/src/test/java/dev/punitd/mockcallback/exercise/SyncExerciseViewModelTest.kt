@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.punitd.mockcallback.exercise

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.time.ZonedDateTime


@ExperimentalCoroutinesApi
class SyncExerciseViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val legacyDbManager: LegacyDbManager = mockk()
    private val exerciseDataSource: ExerciseDataSource = mockk()
    private val exerciseRepository = ExerciseRepository(
        legacyDbManager = legacyDbManager,
        exerciseDataSource = exerciseDataSource,
        ioDispatcher = mainDispatcherRule.testDispatcher
    )

    @Test
    fun test_syncing_view_states() = runTest {
        every { legacyDbManager.syncExercise(any(), any()) } answers {
            val callback = secondArg<Runnable>()
            callback.run()
        }
        coEvery { exerciseDataSource.getExerciseSession(any()) } returns generateDummyExerciseSessionRecord()

        val viewModel = SyncExerciseViewModel(exerciseRepository)
        viewModel.isSyncing.test {
            val item1 = awaitItem()
            println("isSyncing: $item1")
            Assert.assertFalse(item1) // Initial value emitted by stateflow

            viewModel.sync()
            val item2 = awaitItem()
            println("isSyncing: $item2")
            Assert.assertTrue(item2) // Syncing started emitted

            advanceUntilIdle()
            val item3 = awaitItem()
            println("isSyncing: $item3")
            Assert.assertFalse(item3) // Syncing finished emitted
        }
    }
}

class MainDispatcherRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
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