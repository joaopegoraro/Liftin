package xyz.joaophp.liftin.data.services.storage

import android.net.Uri
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

interface StorageService {

    suspend fun upload(path: String, fileUri: Uri): Either<Failure, Uri?>
    suspend fun download(path: String): Either<Failure, ByteArray>
    suspend fun delete(path: String): Either<Failure, Unit>
}