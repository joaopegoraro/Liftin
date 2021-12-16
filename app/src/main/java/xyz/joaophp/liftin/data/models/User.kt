package xyz.joaophp.liftin.data.models

import xyz.joaophp.liftin.utils.*

data class User(
    val uid: String,
) : Model {

    override fun toMap(): HashMap<String, Any> {
        return hashMapOf("uid" to uid)
    }

    override fun fromMap(map: Map<String, Any>): Either<Failure, Model> {
        return try {
            Success(
                User(uid = map["uid"] as String)
            )
        } catch (e: Exception) {
            Error(ConversionFailure())
        }
    }
}

