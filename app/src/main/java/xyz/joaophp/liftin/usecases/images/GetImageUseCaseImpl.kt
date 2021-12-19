package xyz.joaophp.liftin.usecases.images

import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.repositories.image.ImageRepository
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure
import javax.inject.Inject

class GetImageUseCaseImpl @Inject constructor(
    private val imageRepository: ImageRepository
) : GetImageUseCase {

    override suspend fun invoke(exercise: Exercise): Either<Failure, ByteArray> {
        val path = exercise.imagemUrl
        return imageRepository.getImage(path)
    }
}