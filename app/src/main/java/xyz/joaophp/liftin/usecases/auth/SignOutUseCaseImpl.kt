package xyz.joaophp.liftin.usecases.auth

import xyz.joaophp.liftin.data.repositories.user.UserRepository
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure
import javax.inject.Inject

class SignOutUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : SignOutUseCase {

    override fun invoke(): Either<Failure, Unit> {
        return userRepository.signOut()
    }
}