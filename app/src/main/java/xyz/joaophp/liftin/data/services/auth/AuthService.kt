package xyz.joaophp.liftin.data.services.auth

import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

interface AuthService {

    fun getCurrentUser(): Either<Failure, User>
    suspend fun register(email: String, password: String): Either<Failure, User>
    suspend fun signIn(email: String, password: String): Either<Failure, User>
    suspend fun signInAnonymously(): Either<Failure, User>
    fun signOut(): Either<Failure, Unit>
}