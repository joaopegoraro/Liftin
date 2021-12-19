package xyz.joaophp.liftin.data.services.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.tasks.await
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.Failure
import xyz.joaophp.liftin.utils.failures.StorageFailure
import javax.inject.Inject

class StorageServiceImpl @Inject constructor(
    firebaseStorage: FirebaseStorage
) : StorageService {

    companion object {
        private const val ONE_MEGABYTE: Long = 1024 * 1024
    }

    private val storageRef = firebaseStorage.reference

    override suspend fun upload(path: String, fileUri: Uri): Either<Failure, Uri?> {
        return try {
            val ref = storageRef.child(path)
            val uploadUri = ref.putFile(fileUri).await().uploadSessionUri
            Success(uploadUri)
        } catch (e: Exception) {
            val failure = getFailure(e)
            Error(failure)
        }
    }

    override suspend fun download(path: String): Either<Failure, ByteArray> {
        return try {
            val ref = storageRef.child(path)
            val result = ref.getBytes(ONE_MEGABYTE).await()
            Success(result)
        } catch (e: Exception) {
            val failure = getFailure(e)
            Error(failure)
        }
    }

    override suspend fun delete(path: String): Either<Failure, Unit> {
        return try {
            val ref = storageRef.child(path)
            ref.delete().await()
            Success(Unit)
        } catch (e: Exception) {
            val failure = getFailure(e)
            Error(failure)
        }
    }

    private fun getFailure(e: Exception?): StorageFailure {
        return if (e is StorageException) {
            when (e.errorCode) {
                StorageException.ERROR_OBJECT_NOT_FOUND -> StorageFailure.NotFound
                StorageException.ERROR_QUOTA_EXCEEDED -> StorageFailure.LimitExceeded
                StorageException.ERROR_NOT_AUTHENTICATED -> StorageFailure.Unauthorised
                StorageException.ERROR_NOT_AUTHORIZED -> StorageFailure.Unauthorised
                StorageException.ERROR_RETRY_LIMIT_EXCEEDED -> StorageFailure.Timeout
                else -> StorageFailure.Unknown(e)
            }
        } else {
            StorageFailure.Unknown(e)
        }
    }

}