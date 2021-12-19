package xyz.joaophp.liftin.utils.failures

sealed class RegisterFailure(e: Exception? = null) : Failure(e) {
    object UserAlreadyLogged : RegisterFailure()
}
