package xyz.joaophp.liftin.usecases.auth

import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.repositories.user.UserRepository
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

class GetUserUseCaseImpl(
    private val userRepository: UserRepository
) : GetUserUseCase {

    override fun invoke(): Either<Failure, User> {
        return userRepository.getCurrentUser()
    }
}