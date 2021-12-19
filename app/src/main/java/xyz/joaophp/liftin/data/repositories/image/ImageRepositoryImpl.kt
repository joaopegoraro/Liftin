package xyz.joaophp.liftin.data.repositories.image

import android.net.Uri
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.services.storage.StorageService
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.failures.Failure
import xyz.joaophp.liftin.utils.failures.ImageFailure
import java.util.*

class ImageRepositoryImpl(
    private val storageService: StorageService
) : ImageRepository {

    // Generates an unique path
    private val path = fun(user: User): String = "images/${user.uid}/${UUID.randomUUID()}"

    override suspend fun saveImage(user: User, fileUri: Uri): Either<Failure, Uri?> {
        return try {
            val uploadPath = path(user)
            storageService.upload(uploadPath, fileUri)
        } catch (e: Exception) {
            Error(ImageFailure.FailedUpload(e))
        }
    }

    override suspend fun getImage(imagePath: String): Either<Failure, ByteArray> {
        return storageService.download(imagePath)
    }

    override suspend fun deleteImage(imagePath: String): Either<Failure, Unit> {
        return storageService.delete(imagePath)
    }
}