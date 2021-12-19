package xyz.joaophp.liftin.usecases.auth

import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

interface SignOutUseCase {

    operator fun invoke(): Either<Failure, Unit>
}