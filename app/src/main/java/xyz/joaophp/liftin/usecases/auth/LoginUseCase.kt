package xyz.joaophp.liftin.usecases.auth

import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

interface LoginUseCase {
    suspend operator fun invoke(email: String, password: String): Either<Failure, User>
}