package xyz.joaophp.liftin.ui.state

import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout

sealed class AppState(user: User?) {
    class InAuth(user: User?) : AppState(user)
    class InHome(val user: User) : AppState(user)
    class InAddWorkout(user: User) : AppState(user)
    class InWorkout(user: User, workout: Workout) : AppState(user)
    class InAddExercise(user: User, workout: Workout) : AppState(user)
    class InExercise(user: User, exercise: Exercise) : AppState(user)
}
