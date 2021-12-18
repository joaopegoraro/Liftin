package xyz.joaophp.liftin.data.models

import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

abstract class Model {
    abstract fun toMap(): HashMap<String, Any>
    protected abstract fun fromMap(map: Map<String, Any>): Model
}