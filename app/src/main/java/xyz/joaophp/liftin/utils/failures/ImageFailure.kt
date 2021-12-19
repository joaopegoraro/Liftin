package xyz.joaophp.liftin.utils.failures

sealed class ImageFailure(e: Exception?) : Failure(e) {
    class FailedUpload(e: Exception?) : ImageFailure(e)
}
