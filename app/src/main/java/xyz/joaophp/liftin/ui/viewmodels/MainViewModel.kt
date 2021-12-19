package xyz.joaophp.liftin.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import xyz.joaophp.liftin.ui.state.AppState

// App wide ViewModel for managing app state
class MainViewModel : ViewModel() {

    private val appStateFlow = MutableSharedFlow<AppState>()
    val appState: SharedFlow<AppState> = appStateFlow

    fun updateState(state: AppState) {
        viewModelScope.launch {
            appStateFlow.emit(state)
        }
    }
}