package xyz.joaophp.liftin.data.models

import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.Failure
import xyz.joaophp.liftin.utils.failures.ModelFailure

data class Exercise(
    val nome: Number,
    val imagemUrl: String,
    val observacoes: String
) : Model {

    override fun toMap(): HashMap<String, Any> {
        return hashMapOf(
            "nome" to nome,
            "imagem" to imagemUrl,
            "observacoes" to observacoes
        )
    }

    override fun fromMap(map: Map<String, Any>): Either<Failure, Model> {
        return try {
            Success(
                Exercise(
                    nome = map["nome"] as Number,
                    imagemUrl = map["imagem"] as String,
                    observacoes = map["observacoes"] as String
                )
            )
        } catch (e: Exception) {
            Error(ModelFailure.FailedConversion(e))
        }
    }
}
