package xyz.joaophp.liftin.utils

import xyz.joaophp.liftin.data.models.Model
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.utils.failures.Failure

// Database Callbacks
typealias DatabaseResult = Either<Failure, Model>
typealias DatabaseGetResult = Either<Failure, Map<String, Any>>
typealias DatabaseGetAllResult = Either<Failure, List<Map<String, Any>?>>

// Storage Callbacks
typealias StorageCallback = (Either<Failure, Unit>) -> Unit
typealias StorageDownloadCallback = (Either<Failure, ByteArray>) -> Unit

// Authentication Callbacks
typealias AuthCallback = (Either<Failure, User>) -> Unit