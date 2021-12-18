package xyz.joaophp.liftin.data.services.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.StorageCallback
import xyz.joaophp.liftin.utils.StorageDownloadCallback
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.StorageFailure

class StorageServiceImpl(
    firebaseStorage: FirebaseStorage
) : StorageService {

    companion object {
        private const val ONE_MEGABYTE: Long = 1024 * 1024
    }

    private val storageRef = firebaseStorage.reference

    override fun upload(path: String, fileUri: Uri, cb: StorageCallback) {
        val ref = storageRef.child(path)
        ref.putFile(fileUri).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                cb(Success(Unit))
            } else {
                val failure = getFailure(task.exception)
                cb(Error(failure))
            }
        }
    }

    override fun download(path: String, cb: StorageDownloadCallback) {
        val ref = storageRef.child(path)
        ref.getBytes(ONE_MEGABYTE).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                cb(Success(task.result))
            } else {
                val failure = getFailure(task.exception)
                cb(Error(failure))
            }
        }
    }

    override fun delete(path: String, cb: StorageCallback) {
        val ref = storageRef.child(path)
        ref.delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                cb(Success(Unit))
            } else {
                val failure = getFailure(task.exception)
                cb(Error(failure))
            }
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