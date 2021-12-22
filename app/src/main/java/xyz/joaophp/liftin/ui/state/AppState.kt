package xyz.joaophp.liftin.ui.state

import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout

sealed class AppState {
    object InAuth : AppState()
    class InHome(val user: User) : AppState()
    class InAddWorkout(val user: User) : AppState()
    class InWorkout(val user: User, val workout: Workout) : AppState()
    class InAddExercise(val user: User, val workout: Workout) : AppState()
    class InExercise(val user: User, val workout: Workout, val exercise: Exercise) : AppState()
}
