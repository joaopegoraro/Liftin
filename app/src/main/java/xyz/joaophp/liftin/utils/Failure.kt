package xyz.joaophp.liftin.utils

interface Failure

// Common Failures
object UnknownFailure : Failure
class ExceptionFailure(e: Exception) : Failure

// Model Failures
object ConversionFailure : Failure

// Auth Failures
object UserNotFoundFailure : Failure
object RegisterFailure : Failure
object SignInFailure : Failure
object SignOutFailure : Failure

// Firestore Failures
object CreateDocumentFailure : Failure
object ReadDocumentFailure : Failure
object DocumentNotFoundFailure : Failure
object DeleteDocumentFailure : Failure

// Storage Failures
object FileUploadFailure : Failure