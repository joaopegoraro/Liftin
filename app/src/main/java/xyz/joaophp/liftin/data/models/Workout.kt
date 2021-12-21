package xyz.joaophp.liftin.data.models

import xyz.joaophp.liftin.utils.ConversionException

data class Workout(
    val nome: Number,
    val descricao: String,
    val timestamp: Long,
) : Model() {

    companion object {
        fun fromMap(map: Map<String, Any>): Model {
            try {
                return Workout(
                    nome = map["nome"] as Number,
                    descricao = map["descricao"] as String,
                    timestamp = map["timestamp"] as Long
                )
            } catch (e: Exception) {
                throw ConversionException()
            }
        }
    }

    override fun toMap(): HashMap<String, Any> {
        return hashMapOf(
            "nome" to nome,
            "descricao" to descricao,
            "timestamp" to timestamp
        )
    }

    override fun fromMap(map: Map<String, Any>): Model {
        return Companion.fromMap(map)
    }
}
