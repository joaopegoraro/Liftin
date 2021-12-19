package xyz.joaophp.liftin.utils.failures

sealed class LoginFailure(e: Exception? = null) : Failure(e) {
    object UserAlreadyLogged : LoginFailure()
}
