package xyz.joaophp.liftin.data.models

import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.Failure
import xyz.joaophp.liftin.utils.failures.ModelFailure
import java.sql.Timestamp

data class Workout(
    val nome: Number,
    val descricao: String,
    val timestamp: Timestamp,
) : Model {

    override fun toMap(): HashMap<String, Any> {
        return hashMapOf(
            "nome" to nome,
            "descricao" to descricao,
            "timestamp" to timestamp
        )
    }

    override fun fromMap(map: Map<String, Any>): Either<Failure, Model> {
        return try {
            Success(
                Workout(
                    nome = map["nome"] as Number,
                    descricao = map["descricao"] as String,
                    timestamp = map["timestamp"] as Timestamp
                )
            )
        } catch (e: Exception) {
            Error(ModelFailure.FailedConversion(e))
        }
    }
}
