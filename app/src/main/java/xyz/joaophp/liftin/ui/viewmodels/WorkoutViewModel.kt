package xyz.joaophp.liftin.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.usecases.exercises.DeleteExerciseUseCase
import xyz.joaophp.liftin.usecases.exercises.GetExercisesUseCase
import xyz.joaophp.liftin.usecases.images.DeleteImageUseCase
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val getExercisesUseCase: GetExercisesUseCase,
    private val deleteExerciseUseCase: DeleteExerciseUseCase,
    private val deleteImageUseCase: DeleteImageUseCase
) : ViewModel() {

    @ExperimentalCoroutinesApi
    suspend fun getExercises(user: User, workout: Workout) = getExercisesUseCase(user, workout)

    suspend fun deleteExercise(user: User, workout: Workout, exercise: Exercise) =
        deleteExerciseUseCase(user, workout, exercise)

    suspend fun deleteImage(exercise: Exercise): Either<Failure, Unit> {
        return deleteImageUseCase(exercise)
    }

}