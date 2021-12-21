package xyz.joaophp.liftin.utils.failures

sealed class ImageFailure(e: Exception? = null) : Failure(e) {
    object PermissionNeeded : ImageFailure()
    object EmptyField : ImageFailure()
}
