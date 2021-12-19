package xyz.joaophp.liftin.usecases.images

import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

interface GetImageUseCase {

    suspend operator fun invoke(exercise: Exercise): Either<Failure, ByteArray>
}