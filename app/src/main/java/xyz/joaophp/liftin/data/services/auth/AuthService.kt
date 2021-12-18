package xyz.joaophp.liftin.data.services.auth

import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.utils.AuthCallback
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

interface AuthService {

    fun getCurrentUser(): Either<Failure, User>

    fun register(email: String, password: String, cb: AuthCallback)

    fun signIn(email: String, password: String, cb: AuthCallback)

    fun signInAnonymously(cb: AuthCallback)

    fun signOut(): Either<Failure, Unit>
}