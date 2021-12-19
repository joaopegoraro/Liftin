package xyz.joaophp.liftin.usecases.images

import android.net.Uri
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.repositories.image.ImageRepository
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.Failure
import xyz.joaophp.liftin.utils.failures.ImageFailure
import javax.inject.Inject

class CreateImageUseCaseImpl @Inject constructor(
    private val imageRepository: ImageRepository
) : CreateImageUseCase {

    override suspend fun invoke(user: User, fileUri: Uri): Either<Failure, Uri> {
        return imageRepository.saveImage(user, fileUri).fold(
            ifError = { Error(it) },
            ifSuccess = {
                if (it == null) {
                    Error(ImageFailure.ImageNotFound)
                } else {
                    Success(it)
                }
            }
        )
    }
}