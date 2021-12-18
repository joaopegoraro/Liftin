package xyz.joaophp.liftin.utils.failures

sealed class DatabaseFailure(e: Exception? = null) : Failure(e) {
    class Unknown(e: Exception?) : DatabaseFailure(e)
    object DocumentAlreadyExists : DatabaseFailure()
    object InvalidQuery : DatabaseFailure()
    object Timeout : DatabaseFailure()
    object NotFound : DatabaseFailure()
    object Unavailable : DatabaseFailure()
    object DataLost : DatabaseFailure()
    object Unauthorised : DatabaseFailure()
}
