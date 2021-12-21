package xyz.joaophp.liftin.data.models

import xyz.joaophp.liftin.utils.ConversionException

data class Exercise(
    val nome: Number,
    val imagemUrl: String,
    val observacoes: String
) : Model() {

    companion object {
        fun fromMap(map: Map<String, Any>): Exercise {
            try {
                return Exercise(
                    nome = map["nome"] as Number,
                    imagemUrl = map["imagem"] as String,
                    observacoes = map["observacoes"] as String
                )
            } catch (e: Exception) {
                throw ConversionException()
            }
        }
    }

    override fun toMap(): HashMap<String, Any> {
        return hashMapOf(
            "nome" to nome,
            "imagem" to imagemUrl,
            "observacoes" to observacoes
        )
    }

    override fun fromMap(map: Map<String, Any>): Model {
        return Companion.fromMap(map)
    }
}
