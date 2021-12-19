package xyz.joaophp.liftin.usecases.auth

import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.repositories.user.UserRepository
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.Failure
import xyz.joaophp.liftin.utils.failures.LoginFailure
import javax.inject.Inject

class RegisterUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : RegisterUseCase {

    private val registerErrorFold: (Failure) -> Either<Failure, User> =
        { Error(it) }
    private val registerSuccessFold: (User) -> Either<Failure, User> =
        { Success(it) }

    override suspend fun invoke(email: String, password: String): Either<Failure, User> {
        return userRepository.isUserLoggedIn().foldAsync(
            ifError = { Error(it) },
            ifSuccess = { logged ->
                if (logged) {
                    Error(LoginFailure.UserAlreadyLogged)
                } else {
                    userRepository.register(email, password)
                        .fold(registerErrorFold, registerSuccessFold)
                }
            }
        )
    }
}