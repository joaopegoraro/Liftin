package xyz.joaophp.liftin.data.services.auth

import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.Failure

interface AuthService {

    fun getCurrentUser(): Either<Failure, User>

    fun register(email: String, password: String, cb: (Either<Failure, User>) -> Unit)

    fun signIn(email: String, password: String, cb: (Either<Failure, User>) -> Unit)

    fun signInAnonymously(cb: (Either<Failure, User>) -> Unit)

    fun signOut(): Either<Failure, Unit>
}