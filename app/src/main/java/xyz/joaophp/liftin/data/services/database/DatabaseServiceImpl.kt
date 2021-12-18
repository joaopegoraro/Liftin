package xyz.joaophp.liftin.data.services.database

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import xyz.joaophp.liftin.data.models.Model
import xyz.joaophp.liftin.utils.DatabaseCallback
import xyz.joaophp.liftin.utils.DatabaseGetCallback
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.DatabaseFailure

class DatabaseServiceImpl(
    private val db: FirebaseFirestore
) : DatabaseService {

    override fun set(model: Model, path: String, cb: DatabaseCallback) {
        db.document(path).set(model.toMap())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    cb(Success(model))
                } else {
                    val failure = getFailure(task.exception)
                    cb(Error(failure))
                }
            }
    }

    override fun get(path: String, cb: DatabaseGetCallback) {
        db.document(path).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val data = task.result.data ?: mapOf()
                    cb(Success(data))
                } else {
                    val failure = getFailure(task.exception)
                    cb(Error(failure))
                }
            }
    }

    override fun delete(model: Model, path: String, cb: DatabaseCallback) {
        db.document(path).delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    cb(Success(model))
                } else {
                    val failure = getFailure(task.exception)
                    cb(Error(failure))
                }
            }
    }

    /**
     * Returns the [DatabaseFailure] that matches the relevant [FirebaseFirestoreException.code]
     * from the [FirebaseFirestoreException]
     */
    private fun getFailure(e: Exception?): DatabaseFailure {
        return if (e is FirebaseFirestoreException) {
            when (e.code.value()) {
                3 -> DatabaseFailure.InvalidQuery // INVALID_ARGUMENT
                4 -> DatabaseFailure.Timeout // DEADLINE_EXCEEDED
                5 -> DatabaseFailure.NotFound // NOT_FOUND
                6 -> DatabaseFailure.DocumentAlreadyExists // ALREADY_EXISTS
                14 -> DatabaseFailure.Unavailable // UNAVAILABLE
                15 -> DatabaseFailure.DataLost // DATA_LOSS
                16 -> DatabaseFailure.Unauthorised // UNAUTHENTICATED
                else -> DatabaseFailure.Unknown(e)
            }
        } else {
            DatabaseFailure.Unknown(e)
        }
    }
}