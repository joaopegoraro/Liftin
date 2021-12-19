package xyz.joaophp.liftin.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import xyz.joaophp.liftin.ui.state.AppState

// App wide ViewModel for managing app state
class MainViewModel : ViewModel() {

    private val _appState = MutableSharedFlow<AppState>()
    val appState = _appState.asSharedFlow()

    fun updateState(state: AppState) {
        viewModelScope.launch {
            _appState.emit(state)
        }
    }
}