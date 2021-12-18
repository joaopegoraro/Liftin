package xyz.joaophp.liftin.utils

import xyz.joaophp.liftin.data.models.Model
import xyz.joaophp.liftin.data.models.User

typealias DatabaseCallback = ((Either<Failure, Model>) -> Unit)?
typealias DatabaseGetCallback = ((Either<Failure, Map<String, Any>>) -> Unit)?

typealias StorageUploadCallback = ((Either<Failure, Unit>) -> Unit)?

typealias AuthCallback = ((Either<Failure, User>) -> Unit)?