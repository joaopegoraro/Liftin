package xyz.joaophp.liftin.data.models

abstract class Model {
    abstract fun toMap(): HashMap<String, Any>
    protected abstract fun fromMap(map: Map<String, Any>): Model
}