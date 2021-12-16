package xyz.joaophp.liftin.data.services.database

import xyz.joaophp.liftin.data.models.Model
import xyz.joaophp.liftin.utils.DatabaseCallback
import xyz.joaophp.liftin.utils.DatabaseGetCallback

interface DatabaseService {
    fun set(model: Model, path: String, cb: DatabaseCallback)
    fun get(path: String, cb: DatabaseGetCallback)
    fun delete(model: Model, path: String, cb: DatabaseCallback)
}