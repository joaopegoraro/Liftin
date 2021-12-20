package xyz.joaophp.liftin.utils.failures

sealed class WorkoutFailure(e: Exception? = null) : Failure(e) {
    class CreationFailure(e: Exception?) : WorkoutFailure(e)
    object WrongModel : WorkoutFailure()
    object EmptyFields : WorkoutFailure()
}
