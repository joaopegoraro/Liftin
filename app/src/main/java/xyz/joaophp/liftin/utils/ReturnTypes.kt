package xyz.joaophp.liftin.utils

import xyz.joaophp.liftin.utils.failures.Failure

// Database Callbacks
typealias DatabaseGetResult = Either<Failure, Map<String, Any>>
typealias DatabaseGetAllResult = Either<Failure, List<Map<String, Any>?>>

// Storage Callbacks
typealias StorageCallback = (Either<Failure, Unit>) -> Unit
typealias StorageDownloadCallback = (Either<Failure, ByteArray>) -> Unit
