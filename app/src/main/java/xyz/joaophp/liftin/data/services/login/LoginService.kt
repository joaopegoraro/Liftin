package xyz.joaophp.liftin.data.services.login

import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.Failure

interface LoginService {

    fun getCurrentUser(): Either<Failure, User>

    fun signIn(email: String, password: String, cb: (Either<Failure, User>) -> Unit)

    fun signInAnonymously(cb: (Either<Failure, User>) -> Unit)

    fun signOut()
}