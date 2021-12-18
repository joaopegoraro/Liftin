package xyz.joaophp.liftin.data.models

import xyz.joaophp.liftin.utils.ConversionException

data class User(
    val uid: String,
) : Model() {

    companion object {
        fun fromMap(map: Map<String, Any>): Model {
            try {
                return User(uid = map["uid"] as String)
            } catch (e: Exception) {
                throw ConversionException()
            }
        }
    }

    override fun toMap(): HashMap<String, Any> {
        return hashMapOf("uid" to uid)
    }

    override fun fromMap(map: Map<String, Any>): Model {
        return Companion.fromMap(map)
    }
}

