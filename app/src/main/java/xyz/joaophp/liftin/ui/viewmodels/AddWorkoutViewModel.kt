package xyz.joaophp.liftin.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.usecases.workouts.CreateWorkoutUseCase
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Helpers
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.Failure
import xyz.joaophp.liftin.utils.failures.WorkoutFailure
import javax.inject.Inject

@HiltViewModel
class AddWorkoutViewModel @Inject constructor(
    private val createWorkoutUseCase: CreateWorkoutUseCase
) : ViewModel() {

    suspend fun createWorkout(
        nome: Number,
        descricao: String,
        dateString: String,
        user: User
    ): Either<Failure, Unit> {
        return try {
            val timestamp = Helpers.stringToEpoch(dateString)
            val workout = Workout(nome, descricao, timestamp)

            createWorkoutUseCase(workout, user).fold(
                ifError = { Error(it) },
                ifSuccess = { Success(Unit) }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Error(WorkoutFailure.CreationFailure(e))
        }
    }
}