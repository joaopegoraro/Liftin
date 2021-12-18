package xyz.joaophp.liftin.utils.failures

sealed class StorageFailure(e: Exception? = null) : Failure(e) {
    class Unknown(e: Exception?) : StorageFailure(e)
    object NotFound : StorageFailure()
    object LimitExceeded : StorageFailure()
    object Unauthorised : StorageFailure()
    object Timeout : StorageFailure()
}
