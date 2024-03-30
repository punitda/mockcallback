package dev.punitd.mockcallback.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SyncExerciseViewModel(
    private val repository: ExerciseRepository
) : ViewModel() {

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing = _isSyncing.asStateFlow()

    fun sync() {
        viewModelScope.launch {
            _isSyncing.value = true
            repository.syncExercise()
            _isSyncing.value = false
        }
    }
}