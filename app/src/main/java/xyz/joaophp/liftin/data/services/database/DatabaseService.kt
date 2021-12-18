package xyz.joaophp.liftin.data.services.database

import xyz.joaophp.liftin.data.models.Model
import xyz.joaophp.liftin.utils.DatabaseGetAllResult
import xyz.joaophp.liftin.utils.DatabaseGetResult
import xyz.joaophp.liftin.utils.DatabaseResult

interface DatabaseService {
    suspend fun set(model: Model, path: String): DatabaseResult
    suspend fun get(path: String): DatabaseGetResult
    suspend fun getAll(path: String): DatabaseGetAllResult
    suspend fun delete(model: Model, path: String): DatabaseResult
}