package xyz.joaophp.liftin.utils.failures

sealed class ExerciseFailure(e: Exception? = null) : Failure(e) {
    object WrongModel : ExerciseFailure()
}
