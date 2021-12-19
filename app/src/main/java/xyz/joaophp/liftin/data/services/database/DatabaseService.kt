package xyz.joaophp.liftin.data.services.database

import kotlinx.coroutines.ExperimentalCoroutinesApi
import xyz.joaophp.liftin.data.models.Model
import xyz.joaophp.liftin.utils.DatabaseGetAllResult
import xyz.joaophp.liftin.utils.DatabaseGetResult
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

interface DatabaseService {
    suspend fun set(model: Model, path: String): Either<Failure, Model>
    suspend fun get(path: String): DatabaseGetResult
    suspend fun delete(model: Model, path: String): Either<Failure, Model>

    @ExperimentalCoroutinesApi
    fun getAll(path: String): DatabaseGetAllResult

}