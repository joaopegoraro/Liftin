package xyz.joaophp.liftin.usecases.images

import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.repositories.image.ImageRepository
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

class DeleteImageUseCaseImpl(
    private val imageRepository: ImageRepository
) : DeleteImageUseCase {

    override suspend fun invoke(exercise: Exercise): Either<Failure, Unit> {
        val path = exercise.imagemUrl
        return imageRepository.deleteImage(path)
    }
}