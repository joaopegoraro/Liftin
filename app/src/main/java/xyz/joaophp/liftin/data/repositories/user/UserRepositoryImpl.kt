package xyz.joaophp.liftin.data.repositories.user

import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.services.auth.AuthService
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.AuthFailure
import xyz.joaophp.liftin.utils.failures.Failure

class UserRepositoryImpl(
    private val authService: AuthService
) : UserRepository {

    override fun getCurrentUser(): Either<Failure, User> {
        return authService.getCurrentUser()
    }

    override fun isUserLoggedIn(): Either<Failure, Boolean> {
        return authService.getCurrentUser().fold(
            ifError = {
                if (it is AuthFailure.NoCurrentUser) {
                    Success(false)
                } else {
                    Error(it)
                }
            },
            ifSuccess = { Success(true) }
        )
    }

    override suspend fun register(email: String, password: String): Either<Failure, User> {
        return authService.register(email, password)
    }

    override suspend fun signIn(email: String, password: String): Either<Failure, User> {
        return authService.signIn(email, password)
    }

    override suspend fun signInAnonymously(): Either<Failure, User> {
        return authService.signInAnonymously()
    }

    override fun signOut(): Either<Failure, Unit> {
        return authService.signOut()
    }
}