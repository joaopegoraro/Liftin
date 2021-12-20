package xyz.joaophp.liftin.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import xyz.joaophp.liftin.usecases.auth.LoginUseCase
import xyz.joaophp.liftin.usecases.auth.RegisterUseCase
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {

    suspend fun login(email: String, password: String) = loginUseCase(email, password)
    suspend fun register(email: String, password: String) = registerUseCase(email, password)
}