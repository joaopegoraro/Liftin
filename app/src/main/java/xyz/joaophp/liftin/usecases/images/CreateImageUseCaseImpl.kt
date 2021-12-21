package xyz.joaophp.liftin.usecases.images

import android.net.Uri
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.repositories.image.ImageRepository
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure
import javax.inject.Inject

class CreateImageUseCaseImpl @Inject constructor(
    private val imageRepository: ImageRepository
) : CreateImageUseCase {

    override suspend fun invoke(user: User, fileUri: Uri): Either<Failure, String> {
        return imageRepository.saveImage(user, fileUri)
    }
}