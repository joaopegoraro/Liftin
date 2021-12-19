package xyz.joaophp.liftin.utils

import kotlinx.coroutines.flow.Flow
import xyz.joaophp.liftin.utils.failures.Failure

// Database Callbacks
typealias DatabaseGetResult = Either<Failure, Map<String, Any>>
typealias DatabaseGetAllResult = Flow<Either<Failure, List<Map<String, Any>?>?>>