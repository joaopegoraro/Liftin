package xyz.joaophp.liftin.data.repositories.image

import android.net.Uri
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

interface ImageRepository {

    suspend fun saveImage(user: User, fileUri: Uri): Either<Failure, String>
    suspend fun getImage(imagePath: String): Either<Failure, ByteArray>
    suspend fun deleteImage(imagePath: String): Either<Failure, Unit>
}