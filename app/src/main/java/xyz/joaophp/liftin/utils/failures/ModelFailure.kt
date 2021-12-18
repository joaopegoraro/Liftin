package xyz.joaophp.liftin.utils.failures

sealed class ModelFailure(e: Exception?) : Failure(e) {
    class FailedConversion(e: Exception?) : ModelFailure(e)
}
