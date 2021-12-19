package xyz.joaophp.liftin.utils.failures

sealed class ImageFailure(e: Exception? = null) : Failure(e) {
    class FailedUpload(e: Exception?) : ImageFailure(e)
    object ImageNotFound : ImageFailure()
}
