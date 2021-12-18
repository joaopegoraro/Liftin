package xyz.joaophp.liftin.utils

import xyz.joaophp.liftin.data.models.Model
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.utils.failures.Failure

// Database Callbacks
typealias DatabaseCallback = (Either<Failure, Model>) -> Unit
typealias DatabaseGetCallback = (Either<Failure, Map<String, Any>>) -> Unit

// Storage Callbacks
typealias StorageCallback = (Either<Failure, Unit>) -> Unit
typealias StorageDownloadCallback = (Either<Failure, ByteArray>) -> Unit

// Authentication Callbacks
typealias AuthCallback = (Either<Failure, User>) -> Unit