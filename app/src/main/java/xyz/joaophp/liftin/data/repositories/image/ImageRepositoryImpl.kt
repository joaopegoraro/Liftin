package xyz.joaophp.liftin.data.repositories.image

import android.net.Uri
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.services.storage.StorageService
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure
import java.util.*
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val storageService: StorageService
) : ImageRepository {

    // Generates an unique path
    private val path = fun(user: User): String = "images/${user.uid}/${UUID.randomUUID()}"

    override suspend fun saveImage(user: User, fileUri: Uri): Either<Failure, String> {
        val uploadPath = path(user)
        return storageService.upload(uploadPath, fileUri)
    }

    override suspend fun getImage(imagePath: String): Either<Failure, ByteArray> {
        return storageService.download(imagePath)
    }

    override suspend fun deleteImage(imagePath: String): Either<Failure, Unit> {
        return storageService.delete(imagePath)
    }
}