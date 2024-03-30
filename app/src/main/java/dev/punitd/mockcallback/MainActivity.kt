package dev.punitd.mockcallback

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.punitd.mockcallback.exercise.ExerciseDataSourceImpl
import dev.punitd.mockcallback.exercise.ExerciseRepository
import dev.punitd.mockcallback.exercise.ExerciseRoute
import dev.punitd.mockcallback.exercise.ExerciseSessionRecord
import dev.punitd.mockcallback.exercise.ExerciseType
import dev.punitd.mockcallback.exercise.LegacyDbManager
import dev.punitd.mockcallback.exercise.SyncExerciseViewModel
import dev.punitd.mockcallback.ui.theme.MockCallbackTheme
import kotlinx.coroutines.Dispatchers
import java.time.ZonedDateTime

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<SyncExerciseViewModel> {
        viewModelFactory {
            addInitializer(SyncExerciseViewModel::class) {
                SyncExerciseViewModel(
                    repository = ExerciseRepository(
                        exerciseDataSource = ExerciseDataSourceImpl(),
                        legacyDbManager = LegacyDbManager(),
                        ioDispatcher = Dispatchers.IO
                    )
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MockCallbackTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SyncExerciseScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun SyncExerciseScreen(viewModel: SyncExerciseViewModel) {
    val isSyncing by viewModel.isSyncing.collectAsState()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isSyncing) Text(text = "Syncing...")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.sync() },
            enabled = !isSyncing,
        ) {
            Text(text = "Sync")
        }
    }
}