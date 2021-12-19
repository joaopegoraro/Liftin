package xyz.joaophp.liftin.data.services.database

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import xyz.joaophp.liftin.data.models.Model
import xyz.joaophp.liftin.utils.*
import xyz.joaophp.liftin.utils.failures.DatabaseFailure
import xyz.joaophp.liftin.utils.failures.Failure
import javax.inject.Inject

class DatabaseServiceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : DatabaseService {

    override suspend fun set(model: Model, path: String): Either<Failure, Model> {
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

    override suspend fun delete(model: Model, path: String): Either<Failure, Model> {
        return try {
            db.document(path).delete().await()
            Success(model)
        } catch (e: Exception) {
            val failure = getFailure(e)
            Error(failure)
        }
    }

    @ExperimentalCoroutinesApi
    override fun getAll(path: String): DatabaseGetAllResult {
        return callbackFlow {
            val listener = db.collection(path).addSnapshotListener { data, e ->
                if (e != null) {
                    val failure = getFailure(e)
                    trySend(Error(failure))
                } else {
                    val result = data?.documents?.map { it.data }
                    trySend(Success(result))
                }
            }
            awaitClose { listener.remove() }
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