package xyz.joaophp.liftin.data.services.database

import com.google.firebase.firestore.FirebaseFirestore
import xyz.joaophp.liftin.data.models.Model
import xyz.joaophp.liftin.utils.*

class DatabaseServiceImpl(
    private val db: FirebaseFirestore
) : DatabaseService {

    override fun set(model: Model, path: String, cb: DatabaseCallback) {
        try {
            db.document(path).set(model.toMap())
                .addOnSuccessListener { cb?.invoke(Success(model)) }
                .addOnFailureListener { cb?.invoke(Error(CreateDocumentFailure)) }
        } catch (e: Exception) {
            cb?.invoke(Error(ExceptionFailure(e)))
        }
    }

    override fun get(path: String, cb: DatabaseGetCallback) {
        try {
            db.document(path).get()
                .addOnSuccessListener { doc ->
                    val data = doc.data ?: mapOf()
                    if (data.isEmpty()) {
                        cb?.invoke(Error(DocumentNotFoundFailure))
                    } else {
                        cb?.invoke(Success(data))
                    }
                }
                .addOnFailureListener { cb?.invoke(Error(ReadDocumentFailure)) }

        } catch (e: Exception) {
            cb?.invoke(Error(ExceptionFailure(e)))
        }
    }

    override fun delete(model: Model, path: String, cb: DatabaseCallback) {
        try {
            db.document(path).delete()
                .addOnSuccessListener { cb?.invoke(Success(model)) }
                .addOnFailureListener { cb?.invoke(Error(DeleteDocumentFailure)) }
        } catch (e: Exception) {
            cb?.invoke(Error(ExceptionFailure(e)))
        }
    }


}