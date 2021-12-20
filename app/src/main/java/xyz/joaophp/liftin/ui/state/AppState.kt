package xyz.joaophp.liftin.ui.state

import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout

sealed class AppState(user: User? = null) {
    object InAuth : AppState()
    class InHome(val user: User) : AppState(user)
    class InAddWorkout(val user: User) : AppState(user)
    class InWorkout(val user: User, val workout: Workout) : AppState(user)
    class InAddExercise(val user: User, val workout: Workout) : AppState(user)
    class InExercise(val user: User, val exercise: Exercise) : AppState(user)
}
