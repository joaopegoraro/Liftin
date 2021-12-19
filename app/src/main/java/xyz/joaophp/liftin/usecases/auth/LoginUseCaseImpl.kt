package xyz.joaophp.liftin.usecases.auth

import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.repositories.user.UserRepository
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.Failure
import xyz.joaophp.liftin.utils.failures.LoginFailure

class LoginUseCaseImpl(
    private val userRepository: UserRepository
) : LoginUseCase {

    private val signInErrorFold: (Failure) -> Either<Failure, User> = { Error(it) }
    private val signInSuccessFold: (User) -> Either<Failure, User> = { Success(it) }

    override suspend fun invoke(email: String, password: String): Either<Failure, User> {
        return userRepository.isUserLoggedIn().foldAsync(
            ifError = { Error(it) },
            ifSuccess = { logged ->
                if (logged) {
                    Error(LoginFailure.UserAlreadyLogged)
                } else {
                    userRepository.signIn(email, password).fold(signInErrorFold, signInSuccessFold)
                }
            }
        )
    }

}