package xyz.joaophp.liftin.data.services.database

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await
import xyz.joaophp.liftin.data.models.Model
import xyz.joaophp.liftin.utils.*
import xyz.joaophp.liftin.utils.failures.DatabaseFailure

class DatabaseServiceImpl(
    private val db: FirebaseFirestore
) : DatabaseService {

    override suspend fun set(model: Model, path: String): DatabaseResult {
        return try {
            db.document(path).set(model.toMap()).await()
            Success(model)
        } catch (e: Exception) {
            val failure = getFailure(e)
            Error(failure)
        }
    }

    override suspend fun get(path: String): DatabaseGetResult {
        return try {
            val snapshot = db.document(path).get().await()
            val data = snapshot.data ?: mapOf()
            Success(data)
        } catch (e: Exception) {
            val failure = getFailure(e)
            Error(failure)
        }
    }

    override suspend fun getAll(path: String): DatabaseGetAllResult {
        return try {
            val snapshot = db.collection(path).get().await()
            val data = snapshot.documents.map { it.data }
            Success(data)
        } catch (e: Exception) {
            val failure = getFailure(e)
            Error(failure)
        }
    }

    override suspend fun delete(model: Model, path: String): DatabaseResult {
        return try {
            db.document(path).delete().await()
            Success(model)
        } catch (e: Exception) {
            val failure = getFailure(e)
            Error(failure)
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