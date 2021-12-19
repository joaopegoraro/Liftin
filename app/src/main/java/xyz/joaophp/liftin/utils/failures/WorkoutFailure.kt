package xyz.joaophp.liftin.utils.failures

sealed class WorkoutFailure(e: Exception? = null) : Failure(e) {
    object WrongModel : WorkoutFailure()
}
