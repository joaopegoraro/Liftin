package xyz.joaophp.liftin.data.models

import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

interface Model {
    fun toMap(): HashMap<String, Any>
    fun fromMap(map: Map<String, Any>): Either<Failure, Model>
}