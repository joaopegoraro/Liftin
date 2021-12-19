package xyz.joaophp.liftin.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import xyz.joaophp.liftin.ui.state.AppState

// App wide ViewModel for managing app state
class AppViewModel : ViewModel() {

    private val _appState = MutableStateFlow<AppState?>(null)
    val appState = _appState.asStateFlow()

    fun updateState(state: AppState) {
        viewModelScope.launch {
            _appState.emit(state)
        }
    }
}